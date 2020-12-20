import pandas as pd
import matplotlib.pyplot as plt

#表の作成
data = {"月日":["2019-12-01","2019-12-02","2019-12-03","2019-12-04","2019-12-05",
              "2019-12-06","2019-12-07","2019-12-08","2019-12-09","2019-12-10" ,
              "2019-12-11","2019-12-12","2019-12-13","2019-12-14","2019-12-15",
              "2019-12-16","2019-12-17","2019-12-18","2019-12-19","2019-12-20"],
        "販売数":[100, 110, 95, 97, 103, 114, 127, 120, 113, 119, 90, 94, 108, 101, 97, 115, 124, 102, 106, 99]}

df = pd.DataFrame(data)

# 移動平均の計算

df["3日間移動平均"]=df["販売数"].rolling(3).mean().round(1)
df["5日間移動平均"]=df["販売数"].rolling(5).mean().round(1)
df["7日間移動平均"]=df["販売数"].rolling(7).mean().round(1)

print(df)

plt.plot(df["月日"], df["販売数"], label="daily")
plt.plot(df["月日"], df["3日間移動平均"], "k--", label="SMA(3)")
plt.plot(df["月日"], df["5日間移動平均"], "r--", label="SMA(5)")
plt.plot(df["月日"], df["7日間移動平均"], "g--", label="SMA(7)")
plt.xticks(rotation=90)
plt.xlabel("date")
plt.ylabel("quantity")
plt.legend()

plt.show()