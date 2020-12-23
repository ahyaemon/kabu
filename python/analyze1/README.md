# analyze1

### やること
- n 日移動平均の相関を見る
    - 始値、高値、安値、終値のいずれか

### 出力
- csv 形式
- カラム
    - ずらした日数
    - 相関係数

### 引数
- 比較元 csv のパス
- 比較先 csv のパス
    - こっちをずらして相関を計算する
- 出力先ディレクトリのパス
- 移動平均の数
- csv のどのカラムを使うか

```shell script
# python ディレクトリで
python analyze1/analyze1.py ../data/chart/9980_ＭＲＫホール.csv ../data/chart/9979_大庄.csv data/analyze/analyze1 3 1
```
