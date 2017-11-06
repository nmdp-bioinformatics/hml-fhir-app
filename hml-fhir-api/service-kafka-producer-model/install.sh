#!/usr/bin/env bash

sh build.sh

mvn install:install-file -Dfile=target/service-kafka-producer-model-2.0.0-SNAPSHOT.jar -DgeneratePom=true -DgroupId=org.nmdp -DartifactId=service-kafka-producer-model -Dversion=2.0.0 -Dpackaging=jar

echo "Successfully build project, installed in maven"
