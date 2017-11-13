#!/usr/bin/env bash

docker rmi web-hml-fhir -f
docker rmi service-hml-fhir -f
docker rmi fhir-submission-consumer -f
docker rmi hml-fhir-consumer -f
