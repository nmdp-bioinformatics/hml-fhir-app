#!/usr/bin/env bash

SRC_PATH=$(pwd)

while getopts ":p:m:y:b:" opt; do
    case $opt in
        p) packages="$OPTARG"
        ;;
        m) model_path="$OPTARG"
        ;;
        y) python_script="$OPTARG"
        ;;
        b) branch="$OPTARG"
        ;;
    esac
done

if [ -z "$model_path" ]; then
    curl -LJO https://github.com/nmdp-bioinformatics/util-swagger-codegen-models/archive/master.zip
    unzip util-swagger-codegen-models-master.zip
    mv util-swagger-codegen-models-master/model_definitions model_definitions/
    rm -rf util-swagger-codegen-models-master
    rm -rf util-swagger-codegen-models-master.zip
    model_path=$SRC_PATH/model_definitions
fi

sh build.sh -p $packages -m $model_path -b $branch -y $python_script

mvn install:install-file -Dfile=target/service-hml-fhir-converter-models-2.0.0-SNAPSHOT.jar -DgeneratePom=true -DgroupId=org.nmdp -DartifactId=service-hml-fhir-converter-models -Dversion=2.0.0 -Dpackaging=jar

rm -rf $model_path

echo "Successfully installed to maven repo."
