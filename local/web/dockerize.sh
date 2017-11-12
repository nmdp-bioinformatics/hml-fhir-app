#!/usr/bin/env bash

docker build -t web-hml-fhir .

#docker run --name web-hml-fhir-converter -p 0.0.0.0:9005:9005 -d web-hml-fhir:latest