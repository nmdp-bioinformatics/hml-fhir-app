#!/usr/bin/env bash

sh build.sh

mvn install:install-file -Dfile=target/hml-fhir-mongo-2.0.0.jar -DgeneratePom=true -DgroupId=org.nmdp -DartifactId=hml-fhir-mongo -Dversion=2.0.0 -Dpackaging=jar

echo "Successfully build project, installed in maven"