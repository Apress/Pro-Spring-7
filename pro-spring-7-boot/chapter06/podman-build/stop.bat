@echo off
cls
echo "... stop container image ..."
podman rm -f chapter06-2-mariadb
