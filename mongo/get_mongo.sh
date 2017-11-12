#!/usr/bin/env bash

docker pull mongo

docker run --name mongo-db -d -p 127.0.0.1:27017:27017 mongo:3.5
