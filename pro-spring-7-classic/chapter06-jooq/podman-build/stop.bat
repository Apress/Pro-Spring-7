@echo off
cls
echo "... stop container image ..."
podman rm -f local-mariadb
