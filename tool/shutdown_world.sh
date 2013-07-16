#!/bin/sh

APP_PIDS=`ps aux | grep java | grep aquarius-world | awk '{print $2}'`

for PID in $APP_PIDS; do
	echo "kill aquarius world" $PID
	kill -9 $PID
done

