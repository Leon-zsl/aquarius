#!/bin/sh

./shutdown_all.sh
sleep 4

echo "start master..."
./start_master.sh
sleep 2

echo "start db..."
./start_db.sh
sleep 2

echo "start world..."
./start_world.sh
sleep 2

echo "start stage..."
./start_stage.sh
sleep 2

echo "start gate..."
./start_gate.sh
sleep 2

echo "start chat..."
./start_chat.sh
sleep 2

echo "start account..."
./start_account.sh
sleep 2

ps aux | grep java