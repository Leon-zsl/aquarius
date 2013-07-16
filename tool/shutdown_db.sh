#!/bin/sh

APP_PIDS=`ps aux | grep java | grep aquarius-db | awk '{print $2}'`

for PID in $APP_PIDS; do
	echo "kill aquarius db" $PID
	kill -9 $PID
done

