#!/usr/bin/env bash

mvn clean install

sh dockerize.sh

cd compose

docker-compose up
