#!/bin/bash

readonly date=$(date +"%Y-%m-%d_%H%M%S")
source python/venv/bin/activate

# data/chard 内の一覧（date.txt を除く）を取得し、組み合わせで実行する
# とりあえず日経との相関だけみる
for f in $(ls data/chart | grep -v date.txt | grep -v 1001)
do
  echo $f
  python python/analyze1/analyze1.py ./data 1001_日経２２５.csv $f $date
done
