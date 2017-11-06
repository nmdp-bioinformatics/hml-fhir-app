#!/bin/sh

SRC_PATH=$(pwd)

while getopts ":p:m:b:y:" opt; do
    case $opt in
        p) packages="$OPTARG"
        ;;
        m) model_path="$OPTARG"
        ;;
        b) branch="$OPTARG"
        ;;
        y) python_script="$OPTARG"
        ;;
    esac
done

if [ -z "$branch" ]; then
    echo "Github branch?"
    read branch
fi

if [ -z "$python_script" ]; then
    echo "Importing model building script from git."
    curl -LJO https://raw.githubusercontent.com/nmdp-bioinformatics/util-swagger-codegen-models/$branch/Download.py
    python_script=$SRC_PATH/Download.py
    echo "Executing script to build swagger-spec.yaml, downloading model definitions from git."
fi

SWAGGER_PATH=$SRC_PATH/src/main/resources/swagger
SWAGGER_PATHS_DIR=$SWAGGER_PATH/paths
SWAGGER_TEMPLATE_PATH=$SWAGGER_PATH/swagger-template.txt

python $python_script -o $SWAGGER_PATH -s $SWAGGER_PATHS_DIR -t $SWAGGER_TEMPLATE_PATH -d definitions -m $model_path -p $packages

echo "Successfully built swagger-spec.yaml file(s), removing build script."

rm -f Download.py

mvn clean install -X

echo "SUCCESS"
