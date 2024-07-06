#! /bin/sh

#Standard option
export JAVA_OPTS="-server \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat \
    -XX:+UseContainerSupport \    
    -XX:InitialRAMPercentage=25.0 \
    -XX:MaxRAMPercentage=50.0 \
    -XX:+PrintFlagsFinal \
    -XX:+UnlockDiagnosticVMOptions \
    -XX:+UnlockExperimentalVMOptions"

#Run process
exec java $JAVA_OPTIONS $JAVA_OTHER_OPTIONS org.springframework.boot.loader.launch.JarLauncher