#!/usr/bin/env bash

sh service-hml-fhir-converter-models/install.sh
sh service-kafka-producer-model/install.sh
sh kafka-producer/install.sh
sh kafka-consumer/install.sh
sh hml-fhir-converter-mongo/install.sh
sh hml-fhir-converter/install.sh
sh fhir-submission/install.sh
