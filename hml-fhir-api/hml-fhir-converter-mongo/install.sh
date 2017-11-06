#!/usr/bin/env bash

sh build.sh

mvn install:install-file -Dfile=target/hml-fhir-mongo-1.3.0-SNAPSHOT.jar -DgeneratePom=true -DgroupId=org.nmdp -DartifactId=hml-fhir-mongo -Dversion=1.3.0 -Dpackaging=jar

echo "Successfully build project, installed in maven"