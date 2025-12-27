#!/bin/bash

echo " ----- Retrieve MongoDB image ----- "
podman pull mongo:latest

echo " ----- Creating MongoDB instances ----- "
podman-compose up -d

sleep 20

echo " ----- Configuring ReplicaSet  ----- "
podman exec mongo1 ./docker-entrypoint-initdb.d/dbinit.sh
