#!/usr/bin/env bash

cp -r $PWD/../../hml-fhir-webapp/web-hml-fhir-conversion-dashboard .

docker build -t web-hml-fhir .

#docker run --name web-hml-fhir-converter -p 0.0.0.0:9005:9005 -d web-hml-fhir:latest
