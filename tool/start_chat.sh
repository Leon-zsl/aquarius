#!/bin/sh
RUNTIME_PARAM="-server -Xms512m -Xmx512m -Xmn128m -Xss128k -XX:+UseParallelGC -XX:+UseBiasedLocking -XX:+AggressiveOpts -XX:+HeapDumpOnOutOfMemoryError"
DEBUG=""
CLSPATH=""

CMDS="java $RUNTIME_PARAM $DEBUG $CLSPATH -jar ../release/aquarius-chat-1.0-SNAPSHOT.jar"
echo $CMDS
nohup $CMDS &