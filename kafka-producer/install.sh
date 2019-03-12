#!/usr/bin/env bash

sh build.sh

mvn install:install-file -Dfile=target/kafka-producer-2.0.0.jar -DgeneratePom=true -DgroupId=org.nmdp -DartifactId=kafka-producer -Dversion=2.0.0 -Dpackaging=jar

echo "Successfully build project, installed in maven"