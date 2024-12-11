#! /bin/sh

## Constants
export JAVA_OPTS="-server \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat \
    -XX:+UseContainerSupport \
    -XX:InitialRAMPercentage=25.0 \
    -XX:MaxRAMPercentage=50.0 \
    -XX:+PrintFlagsFinal \
    -XX:+UnlockDiagnosticVMOptions \
    -XX:+UnlockExperimentalVMOptions \
    -XshowSettings:vm"

# Function for JVM mode
jvm() {
    echo "Execute JVM mode..."
    exec java $JAVA_OPTS $JAVA_OTHER_OPTIONS -jar app.jar
}

# Validate input
if [ -z "$1" ]; then
    echo "Error: Required 'mode' in input with value: 'jvm'"
    exit 1
fi

case "$1" in
    jvm)
        jvm
        ;;
    *)
        echo "Error: Invalid value, must be: 'jvm'"
        exit 1
        ;;
esac
