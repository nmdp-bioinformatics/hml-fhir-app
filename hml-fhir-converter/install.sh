#!/usr/bin/env bash

sh build.sh

mvn install:install-file -Dfile=target/hml-fhir-2.0.0-SNAPSHOT.jar -DgeneratePom=true -DgroupId=org.nmdp -DartifactId=hml-fhir -Dversion=2.0.0 -Dpackaging=jar

echo "Successfully build project, installed in maven"