#!/usr/bin/env bash

set -euo pipefail

# =============================================================================
# Global Variables
# =============================================================================
: "${JAVA_OTHER_OPTIONS:=}"
: "${DEBUG_PORT:=5500}"
: "${IMPORT_SSL_CERTIFICATE:=0}"
: "${REMOTE_SERVICES:=}"
: "${TRUSTSTORE_PASSWORD:=changeit}"
: "${JAVA_CACERTS_PASSWORD:=changeit}"
: "${RUN_MODE:=jvm}"

TRUSTSTORE_FILE="/app/truststore.jks"
JAVA_OPTS=(
    -server
    -XX:+UseStringDeduplication
    -XX:+OptimizeStringConcat
    -XX:+UseContainerSupport
    -XX:InitialRAMPercentage=25.0
    -XX:MaxRAMPercentage=85.0
    -XX:+PrintFlagsFinal
    -XX:+UnlockDiagnosticVMOptions
    -XX:+UnlockExperimentalVMOptions
    -XshowSettings:vm
    "-Djavax.net.ssl.trustStore=$TRUSTSTORE_FILE"
    "-Djavax.net.ssl.trustStorePassword=$TRUSTSTORE_PASSWORD"
)
JAVA_CMD=()
JAVA_DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT"

# =============================================================================
# Logging functions
# =============================================================================
log_info() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - [INFO] - $*"
}

log_warn() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - [WARN] - $*" >&2
}

log_error() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - [ERROR] - $*" >&2
}

# =============================================================================
# Java functions
# =============================================================================
verify_java_home() {
    if [ -z "$JAVA_HOME" ]; then
        log_error "JAVA_HOME is not set"
        exit 1
    fi
}

print_java_version() {
    log_info "Java version in use"
    "$JAVA_HOME/bin/java" -version
}

build_java_cmd() {
    JAVA_CMD=("$JAVA_HOME/bin/java" "${JAVA_OPTS[@]}")
    [ "$RUN_MODE" = "debug" ] && JAVA_CMD+=("$JAVA_DEBUG_OPTS")
    [ -n "$JAVA_OTHER_OPTIONS" ] && JAVA_CMD+=($JAVA_OTHER_OPTIONS)
    log_info "Constructed JAVA_CMD: ${JAVA_CMD[*]}"
}

# =============================================================================
# Import SSL certificates
# =============================================================================
validate_remote_services() {
    local services="$1"
    if [ -z "$services" ]; then
        log_warn "REMOTE_SERVICES is empty."
        return 1
    fi
    if ! echo "$services" | grep -Eq '^[^:]+:[0-9]+(,[^:]+:[0-9]+)*$'; then
        log_warn "REMOTE_SERVICES format is invalid. Must be: host:port,host:port..."
        return 1
    fi
    return 0
}

create_empty_truststore() {
    log_info "Creating truststore at $TRUSTSTORE_FILE with default Java CAs"

    local java_cacerts=""
    for path in \
        "$JAVA_HOME/lib/security/cacerts" \
        "$JAVA_HOME/lib/security/jssecacerts" \
        "$JAVA_HOME/jre/lib/security/cacerts" \
        "$JAVA_HOME/jre/lib/security/jssecacerts"; do
        if [ -f "$path" ]; then
            java_cacerts="$path"
            break
        fi
    done

    if [ -n "$java_cacerts" ]; then
        log_info "Using default Java truststore: $java_cacerts"
        if ! keytool -importkeystore \
            -srckeystore "$java_cacerts" \
            -destkeystore "$TRUSTSTORE_FILE" \
            -srcstorepass "$JAVA_CACERTS_PASSWORD" \
            -deststorepass "$TRUSTSTORE_PASSWORD" \
            -noprompt >/dev/null 2>&1; then
            log_warn "Failed to copy default Java CAs, creating empty truststore"
            create_dummy_truststore
        fi
    else
        log_warn "Default Java truststore not found, creating empty truststore"
        create_dummy_truststore
    fi
}

create_dummy_truststore() {
    keytool -genkeypair -alias dummy -keystore "$TRUSTSTORE_FILE" \
        -storepass "$TRUSTSTORE_PASSWORD" -keypass "$TRUSTSTORE_PASSWORD" \
        -dname "CN=dummy, OU=dummy, O=dummy, L=dummy, S=dummy, C=US" \
        -keyalg RSA -keysize 2048 -validity 1 >/dev/null 2>&1 || true
    keytool -delete -alias dummy -keystore "$TRUSTSTORE_FILE" \
        -storepass "$TRUSTSTORE_PASSWORD" >/dev/null 2>&1 || true
}

import_ssl_certificates() {
    log_info "Configured IMPORT_SSL_CERTIFICATE: $IMPORT_SSL_CERTIFICATE"
    log_info "Configured REMOTE_SERVICES: $REMOTE_SERVICES"

    # Skip if import is disabled
    if [ "$IMPORT_SSL_CERTIFICATE" -ne 1 ]; then
        return 0
    fi

    # Skip if REMOTE_SERVICES is empty or invalid
    if ! validate_remote_services "$REMOTE_SERVICES"; then
        log_warn "Skipping SSL import due to empty or invalid REMOTE_SERVICES."
        return 0
    fi

    # Remove existing truststore
    if [ -f "$TRUSTSTORE_FILE" ]; then
        log_info "Deleting existing truststore at $TRUSTSTORE_FILE"
        rm -f "$TRUSTSTORE_FILE"
    fi

    # Create truststore with default Java CAs
    create_empty_truststore

    # Import external certificates
    IFS=',' read -ra services <<< "$REMOTE_SERVICES"
    for service in "${services[@]}"; do
        # fix #8: parameter expansion al posto di subshell con cut
        host="${service%%:*}"
        port="${service##*:}"
        cert_file="/app/${host}_external_certificate.crt"

        log_info "Fetching SSL certificate from $host:$port..."
        if ! openssl s_client -connect "$host:$port" -servername "$host" -showcerts < /dev/null \
            | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > "$cert_file"; then
            log_error "Failed to fetch certificate from $host:$port"
            rm -f "$cert_file"
            continue
        fi

        log_info "Importing certificate for $host into truststore..."
        if keytool -importcert -noprompt -trustcacerts \
            -alias "external_${host}" \
            -file "$cert_file" \
            -keystore "$TRUSTSTORE_FILE" \
            -storepass "$TRUSTSTORE_PASSWORD"; then
            log_info "Successfully imported certificate for $host"
        else
            log_error "Failed to import certificate for $host"
        fi

        rm -f "$cert_file"
    done
}

# =============================================================================
# Run functions
# =============================================================================
run_app() {
    log_info "Selected run mode: $RUN_MODE"
    case "$RUN_MODE" in
        jvm)
            log_info "Starting application..."
            exec "${JAVA_CMD[@]}" -jar app.jar
            ;;
        debug)
            log_info "Starting application in debug mode on port $DEBUG_PORT..."
            exec "${JAVA_CMD[@]}" -jar app.jar
            ;;
        *)
            log_error "Invalid RUN_MODE '$RUN_MODE'. Allowed values: jvm, debug"
            exit 1
            ;;
    esac
}

# =============================================================================
# Main program
# =============================================================================
verify_java_home
print_java_version
build_java_cmd
import_ssl_certificates
run_app
