#!/bin/sh
RUNTIME_PARAM="-server -Xms512m -Xmx512m -Xmn128m -Xss256k -XX:+UseParallelGC -XX:+UseBiasedLocking -XX:+AggressiveOpts -XX:+HeapDumpOnOutOfMemoryError"
DEBUG=""
CLSPATH=""

pushd ../release
CMDS="java $RUNTIME_PARAM $DEBUG $CLSPATH -jar aquarius-account-1.0-SNAPSHOT.jar"
echo $CMDS
nohup $CMDS &
popd