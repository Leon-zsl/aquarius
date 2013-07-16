#!/bin/sh

./shutdown_all.sh
sleep 4

./start_master.sh
sleep 2

./start_db.sh
sleep 2

./start_world.sh
sleep 2

./start_stage.sh
slepp 2

./start_gate.sh
sleep 2

./start_chat.sh
sleep 2

./start_account.sh
sleep 2