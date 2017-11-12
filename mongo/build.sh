#!/usr/bin/env bash

sh get_mongo.sh
sh install_pyMongo.sh

python CreateDatabase.py
