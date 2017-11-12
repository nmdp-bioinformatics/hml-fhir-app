#!/bin/bash

cd kafka_2.12-0.10.2.1

exec bin/kafka-server-start.sh config/server.properties
