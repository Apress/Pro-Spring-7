#!/bin/sh

echo "... stop container image ..."
podman rm -f local-mariadb
