#!/bin/bash

mkdir -p ./data/adjust
mkdir -p ./data/adjust/split
mkdir -p ./data/adjust/merge

readonly path="./data"

java -jar build/libs/kabu-0.1-all.jar adjust -p=$path
