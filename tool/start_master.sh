#!/bin/sh
RUNTIME_PARAM="-server -Xms256m -Xmx256m -Xmn64m -Xss256k -XX:+UseParallelGC -XX:+UseBiasedLocking -XX:+AggressiveOpts -XX:+HeapDumpOnOutOfMemoryError"
DEBUG=""
CLSPATH=""

pushd ../release
CMDS="java $RUNTIME_PARAM $DEBUG $CLSPATH -jar aquarius-master-1.0-SNAPSHOT.jar"
echo $CMDS
nohup $CMDS &
popd