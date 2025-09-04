#!/bin/sh

echo "Building pro-spring-7 ..."

echo ">> Building pro-spring-7-boot ..."
pushd pro-spring-7-boot
gradle
popd
echo ">> Building pro-spring-7-classic ..."
pushd pro-spring-7-classic
gradle
popd

echo "ALL DONE."
