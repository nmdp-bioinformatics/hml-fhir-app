#!/usr/bin/env bash

sh build.sh

mvn install:install-file -Dfile=target/kafka-producer-1.0.0-SNAPSHOT.jar -DgeneratePom=true -DgroupId=org.nmdp -DartifactId=kafka-producer -Dversion=1.0.0 -Dpackaging=jar

echo "Successfully build project, installed in maven"