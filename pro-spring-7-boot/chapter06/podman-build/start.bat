@echo off
cls

echo "... building container image ..."
podman build -t prospring7-mariadb:6.2 .

echo "... run container image ..."
podman run --name chapter06-2-mariadb -d -p 3306:3306 prospring7-mariadb:6.2
