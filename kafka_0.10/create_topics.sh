#!/bin/bash


cd kafka_2.12-0.10.2.1

sh bin/kafka-topics.sh --create --zookeeper kafka:2181 --replication-factor 1 --partitions 1 --topic fhir-submission
sh bin/kafka-topics.sh --create --zookeeper kafka:2181 --replication-factor 1 --partitions 1 --topic hml-fhir-conversion
