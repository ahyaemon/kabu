from typing import Callable

import pandas as pd
import matplotlib.pyplot as plt
import sys
from sklearn import preprocessing
import os


class Properties:
    def __init__(self, args):
        data_directory = args[1]

        origin_filename = args[2]

        target_filename = args[3]

        output_directory_name = args[4]

        # 比較元銘柄コード
        self.code_origin = origin_filename.split("_")[0]

        # 比較先銘柄コード
        self.code_target = target_filename.split("_")[0]

        # 比較元 csv のパス
        self.origin_path = data_directory + "/chart/" + origin_filename

        # 比較先 csv のパス
        self.target_path = data_directory + "/chart/" + target_filename

        # split ファイルのパス
        self.split_path = data_directory + "/adjust/split/split.csv"

        # merge ファイルのパス
        self.merge_path = data_directory + "/adjust/merge/merge.csv"

        # 出力先ディレクトリのパス
        self.output_directory = data_directory + "/analyze/analyze1/" + output_directory_name + "/" + self.code_origin

        # 移動平均に使用するデータの数
        self.mean_count = 3

        # データ解析に使用する列
        self.column = 1

        # 図を保存するための相関係数の最低値
        self.min_corr = 0.95

        # 図を保存するための日数の最低値
        self.min_used_days = 50


class Preprocessor:
    def __init__(self, split_path: str, merge_path: str, mean_count: int):
        self.split_path = split_path
        self.merge_path = merge_path
        self.mean_count = mean_count

    def preprocess(self, df: pd.DataFrame, code: str) -> pd.DataFrame:
        # 日付順にソート
        df_sorted = df.sort_values(by="date")

        # 株式分割
        df_splitted = self._adjust(df_sorted, self.split_path, code, lambda series, ratio: series * ratio)

        # 株式併合
        df_merged = self._adjust(df_splitted, self.merge_path, code, lambda series, ratio: series / ratio)

        # 移動平均
        df_merged["mean"] = self.mean(df_merged["value"], self.mean_count)

        # 正規化
        df_normalized = self._normalize(df_merged)

        # インデックスを振り直す
        df_normalized.index = pd.RangeIndex(0, len(df), 1)

        return df_normalized

    def _adjust(self, df: pd.DataFrame, path: str, code: str, f: Callable[[pd.Series, float], pd.Series]) -> pd.DataFrame:
        csv = pd.read_csv(path)

        df_merged = df.copy()
        rows = csv[csv["銘柄コード"] == int(code)]
        for row in rows.itertuples(name=None):
            date = row[5]
            df_prev = df_merged[df_merged["date"] <= date]
            df_after = df_merged[df_merged["date"] > date]

            ratio = row[4]
            df_after.loc[:, "value"] = f(df_after.loc[:, "value"], ratio)
            df_merged = df_prev.append(df_after)

        return df_merged

    def _normalize(self, df: pd.DataFrame) -> pd.DataFrame:
        df_normalized = df.copy()
        df_normalized["normalized"] = preprocessing.minmax_scale(df_normalized["mean"])
        return df_normalized

    def mean(self, sr: pd.Series, count: int) -> pd.Series:
        return sr.rolling(count).mean().round(1)


if __name__ == '__main__':
    properties = Properties(sys.argv)
    preprocessor = Preprocessor(properties.split_path, properties.merge_path, properties.mean_count)

    df_origin = pd.read_csv(properties.origin_path, header=None).iloc[:, [0, properties.column]]
    df_target = pd.read_csv(properties.target_path, header=None).iloc[:, [0, properties.column]]

    if len(df_origin) != len(df_target):
        print("invalid data amount. origin: " + str(len(df_origin)) + ", target: " + str(len(df_target)))
        exit()

    # origin
    df_origin.columns = ["date", "value"]
    df_origin_adjusted = preprocessor.preprocess(df_origin, properties.code_origin)

    # target
    df_target.columns = ["date", "value"]
    df_target_adjusted = preprocessor.preprocess(df_target, properties.code_target)

    # 保存先ディレクトリを作成
    os.makedirs(properties.output_directory, exist_ok=True)

    # 1 日ずつずらしながら相関係数を計算する
    # ずらした日数、相関係数、計算に使ったデータの数で csv を作る
    data_amount = len(df_origin_adjusted) - properties.mean_count + 1
    df_corr = pd.DataFrame(columns=["offset", "corr", "used_days"])
    for i in range(data_amount):
        beg_origin = properties.mean_count - 1 + i
        end_origin = len(df_origin_adjusted) - 1
        series_origin = df_origin_adjusted.loc[beg_origin:end_origin, "normalized"]
        series_origin.index = pd.RangeIndex(0, len(series_origin), 1)

        beg_target = properties.mean_count - 1
        end_target = len(df_target_adjusted) - 1 - i
        series_target = df_target_adjusted.loc[beg_target:end_target, "normalized"]
        series_target.index = pd.RangeIndex(0, len(series_target), 1)

        corr = pd.DataFrame({"origin": series_origin, "target": series_target}).corr().iloc[0, 1]

        used_days = len(series_origin)
        df_corr = df_corr.append({"offset": i, "corr": corr, "used_days": used_days}, ignore_index=True)

        # 条件を満たす場合に図を保存する
        if (abs(corr) >= properties.min_corr) and (used_days >= properties.min_used_days):
            print("HIT!!")
            fig = plt.figure()

            series_date = df_origin_adjusted.loc[beg_origin:end_origin, "date"]
            series_origin.index = series_date.index
            series_target.index = series_date.index
            df_plot = pd.DataFrame({"date": series_date, "origin": series_origin, "target": series_target})

            plt.plot(df_plot["date"], df_plot["origin"], label="date")
            plt.plot(df_plot["date"], df_plot["target"], label="date")

            plt.xticks(rotation=90)

            image_filename = properties.code_origin + "_" + properties.code_target + "_" + str(i) + ".jpg"
            fig.savefig(properties.output_directory + "/" + image_filename)

    # TODO 保存対象がひとつでもある場合、元のグラフも保存する


    df_corr = df_corr.astype({"offset": int})
    df_corr = df_corr.astype({"used_days": int})
    output_filename = properties.code_origin + "_" + properties.code_target + ".csv"
    df_corr.to_csv(properties.output_directory + "/" + output_filename)
