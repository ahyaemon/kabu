#!/bin/bash

readonly path="./data"

mkdir -p $path/chart

java -jar build/libs/kabu-0.1-all.jar chart -p=$path
