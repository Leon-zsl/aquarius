#!/bin/sh
RUNTIME_PARAM="-server -Xms256m -Xmx256m -Xmn64m -Xss128k -XX:+UseParallelGC -XX:+UseBiasedLocking -XX:+AggressiveOpts -XX:+HeapDumpOnOutOfMemoryError"
DEBUG=""
CLSPATH=""

CMDS="java $RUNTIME_PARAM $DEBUG $CLSPATH -jar ../release/aquarius-master-1.0-SNAPSHOT.jar"
echo $CMDS
nohup $CMDS &