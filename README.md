# kabu
- 株価を取ってきて分析しようとしている

## build
```
> gradlew build
```

## usage
- bulid/libs/kabu-0.1-all.jar を実行する
- サブコマンド
    - get: 対象日付の株価一覧を取得する
        - `-d="2020-01-06"` みたいな感じで日付指定
        - `-p="/tmp/kabu"` みたいな感じで出力先指定
            - 対象のディレクトリと、その下に `zip` `csv` ディレクトリがあること
    - calc: 分析する
