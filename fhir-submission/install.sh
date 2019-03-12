#!/usr/bin/env bash

sh build.sh

mvn install:install-file -Dfile=target/fhir-submission-2.0.0.jar -DgeneratePom=true -DgroupId=org.nmdp -DartifactId=fhir-submission -Dversion=2.0.0 -Dpackaging=jar

echo "Successfully built project, installed in maven"