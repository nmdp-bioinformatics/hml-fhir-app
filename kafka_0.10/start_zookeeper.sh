#!/bin/bash

cd kafka_2.12-0.10.2.1

exec bin/zookeeper-server-start.sh config/zookeeper.properties
