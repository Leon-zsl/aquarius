#!/bin/sh
RUNTIME_PARAM="-server -Xms1024m -Xmx1024m -Xmn480m -Xss256k -XX:+UseParallelGC -XX:+UseBiasedLocking -XX:+AggressiveOpts -XX:+HeapDumpOnOutOfMemoryError"
DEBUG=""
CLSPATH=""

pushd ../release
CMDS="java $RUNTIME_PARAM $DEBUG $CLSPATH -jar aquarius-db-1.0-SNAPSHOT.jar"
echo $CMDS
nohup $CMDS &
popd