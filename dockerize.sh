#!/usr/bin/env bash

sh service-hml-fhir-converter-api/dockerize.sh
sh process-kafka-hml-fhir-conversion-consumer/dockerize.sh
sh process-kafka-fhir-submission-consumer/dockerize.sh
sh local/web/dockerize.sh
sh kafka_0.10/dockerize.sh
