#!/bin/bash

mkdir -p ./data/chart

readonly path="./data"

java -jar build/libs/kabu-0.1-all.jar chart -p=$path
