#!/bin/bash

readonly path="./data"

mkdir -p $path/adjust
mkdir -p $path/adjust/split
mkdir -p $path/adjust/merge

java -jar build/libs/kabu-0.1-all.jar adjust -p=$path
