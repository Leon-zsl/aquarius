#!/bin/sh

APP_PIDS=`ps aux | grep java | grep aquarius-gate | awk '{print $2}'`

for PID in $APP_PIDS; do
	echo "kill aquarius gate" $PID
	kill -9 $PID
done

