#!/bin/bash

mkdir -p ./data/zip
mkdir -p ./data/csv

readonly count=10
readonly path="./data"

for((i=$count-1; i>=0; i--))
do
  date=$(date -v-${i}d +"%Y-%m-%d")
  echo $date start
  java -jar build/libs/kabu-0.1-all.jar get -d=$date -p=$path
  sleep 1
done
