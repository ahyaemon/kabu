import pandas as pd
import matplotlib.pyplot as plt
import sys

args = sys.argv
origin_path = args[1]
target_path = args[2]
output_directory = args[3]
n = args[4]

if __name__ == '__main__':
    df1 = pd.read_csv(origin_path, header=None)
    mean1 = df1.rolling(n).mean().round(1)

    df2 = pd.read_csv(target_path, header=None)
    mean2 = df2.rolling(n).mean().round(1)

    df = pd.DataFrame()
    df[0] = mean1[5]
    df[1] = mean2[5]

    corr = df.corr()
    print(corr[0][1])
