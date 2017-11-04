#!/usr/bin/env bash

mvn clean install

docker build -f Dockerfile -t demo/hml-fhir-consumer .