#!/bin/sh
set -euo pipefail

# =============================================================================
# Logging functions
# =============================================================================
log_info() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - [INFO] - $*"
}

log_warn() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - [WARN] - $*"
}

log_error() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - [ERROR] - $*"
}

# =============================================================================
# JVM options
# =============================================================================
JAVA_OPTS="-server \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat \
    -XX:+UseContainerSupport \
    -XX:InitialRAMPercentage=25.0 \
    -XX:MaxRAMPercentage=50.0 \
    -XX:+PrintFlagsFinal \
    -XX:+UnlockDiagnosticVMOptions \
    -XX:+UnlockExperimentalVMOptions \
    -XshowSettings:vm"

# =============================================================================
# Global Variables
# =============================================================================
: "${JAVA_OTHER_OPTIONS:=}"
: "${DEBUG_PORT:=5005}"
: "${IMPORT_SSL_CERTIFICATE:=0}"

# =============================================================================
# Import SSL certificates
# =============================================================================
import_ssl_certificates() {

    log_info "IMPORT_SSL_CERTIFICATE: $IMPORT_SSL_CERTIFICATE"
    log_info "REMOTE_SERVICES: $REMOTE_SERVICES"

    if [ "$IMPORT_SSL_CERTIFICATE" -ne 1 ]; then
        log_info "IMPORT_SSL_CERTIFICATE is disabled. Skipping SSL import."
        return 0
    fi

    if [ -z "${REMOTE_SERVICES:-}" ]; then
        log_warn "REMOTE_SERVICES is not configured but IMPORT_SSL_CERTIFICATE=1. Skipping SSL import."
        return 0
    fi

    # Validate REMOTE_SERVICES format: host:port,host:port,...
    if ! echo "$REMOTE_SERVICES" | grep -Eq '^[^:]+:[0-9]+(,[^:]+:[0-9]+)*$'; then
        log_warn "REMOTE_SERVICES format is invalid. Must be: host:port,host:port... Skipping import."
        return 0
    fi

    cacerts_file="$JAVA_HOME/lib/security/cacerts"
    [ ! -f "$cacerts_file" ] && cacerts_file="$JAVA_HOME/jre/lib/security/cacerts"

    if [ ! -f "$cacerts_file" ]; then
        log_error "cacerts file not found in JAVA_HOME: $JAVA_HOME"
        exit 1
    fi

    for service in $(echo "$REMOTE_SERVICES" | tr ',' ' '); do
        host=$(echo "$service" | cut -d':' -f1)
        port=$(echo "$service" | cut -d':' -f2)
        cert_file="${host}_external_certificate.crt"

        log_info "Fetching SSL certificate from $host:$port..."
        openssl s_client -connect "$host:$port" -servername "$host" < /dev/null \
            | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > "$cert_file"

        if [ $? -ne 0 ]; then
            log_error "Failed to fetch certificate from $host:$port"
            rm -f "$cert_file"
            continue
        fi

        log_info "Certificate saved to $cert_file"

        # Check if certificate already exists
        if keytool -list -keystore "$cacerts_file" -storepass changeit -alias "external_${host}" > /dev/null 2>&1; then
            log_info "Certificate for $host already exists in cacerts. Skipping import."
        else
            log_info "Importing $cert_file into cacerts..."
            keytool -importcert -noprompt -trustcacerts \
                -alias "external_${host}" \
                -file "$cert_file" \
                -keystore "$cacerts_file" \
                -storepass changeit \
                && log_info "Certificate successfully imported for $host" \
                || log_error "Failed to import certificate for $host"
        fi

        rm -f "$cert_file"
    done

    log_info "SSL certificate import completed for all services."
}

# =============================================================================
# Build Java command once
# =============================================================================
JAVA_CMD="java $JAVA_OPTS"
[ -n "$JAVA_OTHER_OPTIONS" ] && JAVA_CMD="$JAVA_CMD $JAVA_OTHER_OPTIONS"

# =============================================================================
# Run application
# =============================================================================
run_app() {

    log_info "Java version in use:"
    java -version

    local mode="$1"
    log_info "Selected mode: $mode"

    import_ssl_certificates

    case "$mode" in
        jvm)
            log_info "Starting application..."
            exec $JAVA_CMD -jar app.jar
            ;;
        debug)
            log_info "Starting application on debug port: $DEBUG_PORT..."
            exec $JAVA_CMD -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT -jar app.jar
            ;;
        *)
            log_error "Invalid mode '$mode'. Allowed values: jvm, debug"
            exit 1
            ;;
    esac
}

# Verify if JAVA_HOME in set
if [ -z "${JAVA_HOME:-}" ]; then
    log_error "JAVA_HOME is not set"
    exit 1
fi

MODE=${1:-jvm}
run_app "$MODE"
