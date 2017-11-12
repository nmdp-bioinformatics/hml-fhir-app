#!/usr/bin/env bash

docker build -t service-hml-fhir .

#docker run --name service-hml-fhir-converter -p 127.0.0.1:8099:8099 -d service-hml-fhir:latest
