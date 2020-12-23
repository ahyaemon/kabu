import pandas as pd
import matplotlib.pyplot as plt


if __name__ == '__main__':
    df1 = pd.read_csv("../data/chart/1348_MAXトピックス.csv", header=None)
    mean1 = df1.rolling(3).mean().round(1)

    df2 = pd.read_csv("../data/chart/9973_小僧寿し.csv", header=None)
    mean2 = df2.rolling(3).mean().round(1)

    df = pd.DataFrame()
    df[0] = mean1[5]
    df[1] = mean2[5]

    corr = df.corr()
    print(corr)

    plt.plot(mean1[0], mean1[1], label="daily")
    # plt.plot(mean2[0], mean2[1], label="daily")
    plt.xticks(rotation=90)
    plt.show()