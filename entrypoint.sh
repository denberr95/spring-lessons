#!/bin/sh
set -euo pipefail

log() { 
    echo "$(date '+%Y-%m-%d %H:%M:%S') - [INFO] - $*"; 
}
error() { 
    echo "$(date '+%Y-%m-%d %H:%M:%S') - [ERROR] - $*" >&2; 
}

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

: "${JAVA_OTHER_OPTIONS:=}"
: "${DEBUG_PORT:=5005}"

java_cmd() {
    CMD="java $JAVA_OPTS"
    [ -n "$JAVA_OTHER_OPTIONS" ] && CMD="$CMD $JAVA_OTHER_OPTIONS"
    echo "$CMD"
}

run_jvm() {
    log "MODE=jvm - Starting application..."
    exec $(java_cmd) -jar app.jar
}

run_debug() {
    log "MODE=debug - Starting application on port $DEBUG_PORT..."
    exec $(java_cmd) -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT -jar app.jar
}

MODE=${1:-jvm}

case "$MODE" in
    jvm)   
        run_jvm
        ;;
    debug)
        run_debug
        ;;
    *)
        error "Invalid mode '$MODE'. Allowed values: jvm, debug"
        exit 1
        ;;
esac
