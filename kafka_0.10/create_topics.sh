#!/bin/bash

cd /usr

sh bin/kafka-topics.sh --create --zookeeper kafka:2181 --replication-factor 1 --partitions 1 --topic fhir-submission
sh bin/kafka-topics.sh --create --zookeeper kafka:2181 --replication-factor 1 --partitions 1 --topic hml-fhir-conversion
