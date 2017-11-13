#!/usr/bin/env bash

cd service-hml-fhir-converter-api
sh dockerize.sh

cd ..

cd process-kafka-hml-fhir-conversion-consumer
sh dockerize.sh

cd ..

cd process-kafka-fhir-submission-consumer
sh dockerize.sh

cd ..

cd local/web
sh dockerize.sh
