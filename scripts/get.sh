#!/bin/bash

readonly count=100
readonly path="./data"

mkdir -p $path/zip
mkdir -p $path/csv

for((i=$count-1; i>=0; i--))
do
  date=$(date -v-${i}d +"%Y-%m-%d")
  echo $date start
  java -jar build/libs/kabu-0.1-all.jar get -d=$date -p=$path
  sleep 1
done
