#!/bin/sh

echo "shutdown account..."
./shutdown_account.sh

echo "shutdown chat..."
./shutdown_chat.sh

echo "shutdown gate..."
./shutdown_gate.sh

echo "shutdown stage..."
./shutdown_stage.sh

echo "shutdown world..."
./shutdown_world.sh

echo "shutdown db..."
./shutdown_db.sh

echo "shutdown master..."
./shutdown_master.sh
