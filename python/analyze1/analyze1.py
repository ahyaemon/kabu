from typing import Callable

import pandas as pd
import matplotlib.pyplot as plt
import sys
from sklearn import preprocessing


class Properties:
    def __init__(self, args):
        self.data_directory = args[1]

        self.origin_filename = args[2]

        self.target_filename = args[3]

        # 比較元銘柄コード
        self.code_origin = self.origin_filename.split("_")[0]

        # 比較先銘柄コード
        self.code_target = self.target_filename.split("_")[0]

        # 比較元 csv のパス
        self.origin_path = self.data_directory + "/chart/" + self.origin_filename

        # 比較先 csv のパス
        self.target_path = self.data_directory + "/chart/" + self.target_filename

        # split ファイルのパス
        self.split_path = self.data_directory + "/adjust/split/split.csv"

        # merge ファイルのパス
        self.merge_path = self.data_directory + "/adjust/merge/merge.csv"

        # 出力先ディレクトリのパス
        self.output_directory = self.data_directory + "/analyze/analyze1"

        # 移動平均に使用するデータの数
        self.mean_count = 3

        # データ解析に使用する列
        self.column = 1


class Preprocessor:
    def __init__(self, split_path: str, merge_path: str, mean_count: int):
        self.split_path = split_path
        self.merge_path = merge_path
        self.mean_count = mean_count

    def preprocess(self, df: pd.DataFrame, code: str) -> pd.DataFrame:
        df_splitted = self._adjust(df, self.split_path, code, lambda series, ratio: series * ratio)
        df_merged = self._adjust(df_splitted, self.merge_path, code, lambda series, ratio: series / ratio)
        df_merged["mean"] = self.mean(df_merged["value"], self.mean_count)
        df_normalized = self._normalize(df_merged)

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

    # 1 日ずつずらしながら相関係数を計算する
    # ずらした日数、相関係数、計算に使ったデータの数で csv を作る
    data_amount = len(df_origin_adjusted) - properties.mean_count + 1
    df_corr = pd.DataFrame(columns=["days", "corr", "n"])
    for i in range(data_amount):
        beg_origin = properties.mean_count - 1 + i
        end_origin = len(df_origin_adjusted) - 1
        series_origin = df_origin_adjusted.loc[beg_origin:end_origin, "normalized"]

        beg_target = properties.mean_count - 1
        end_target = len(df_target_adjusted) - 1 - i
        series_target = df_target_adjusted.loc[beg_target:end_target, "normalized"]

        corr = pd.DataFrame({"origin": series_origin, "target": series_target}).corr().iloc[0, 1]

        df_corr = df_corr.append({"days": i, "corr": corr, "n": len(series_origin)}, ignore_index=True)

        # corr が0.95 以上の場合、図を保存する
        if corr >= 0.95:
            fig = plt.figure()

            series_date = df_origin_adjusted.loc[beg_origin:end_origin, "date"]
            series_origin.index = series_date.index
            series_target.index = series_date.index
            df_plot = pd.DataFrame({"date": series_date, "origin": series_origin, "target": series_target})

            plt.plot(df_plot["date"], df_plot["origin"], label="date")
            plt.plot(df_plot["date"], df_plot["target"], label="date")

            plt.xticks(rotation=90)
            plt.show()

            image_filename = properties.code_origin + "_" + properties.code_target + "_" + str(i) + ".jpg"
            fig.savefig(properties.data_directory + "/analyze/analyze1/" + image_filename)

    df_corr = df_corr.astype({"days": int})
    output_filename = properties.code_origin + "_" + properties.code_target + ".csv"
    df_corr.to_csv(properties.data_directory + "/analyze/analyze1/" + output_filename)
