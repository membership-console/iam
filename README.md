# IAM

![CI](https://github.com/membership-console/membership-console-iam/workflows/CI/badge.svg)
![version](https://img.shields.io/badge/version-1.0.0__SNAPSHOT-blue.svg)

## 概要

本プロジェクトはMembership ConsoleのIAM機能を提供します。

## 開発

### 開発環境

- Java OpenJDK 11
- Spring Boot 2.7
- MySQL 8.0
- docker-compose

### ビルド方法

ビルドに成功すると、`app/build/libs`直下に`.jar`ファイルが生成されます。

```sh
$ ./gradlew bootJar
```

### 起動方法

まず、Docker から MySQL を起動します。

```
$ docker compose up -d
# 3306 db-local: ローカル用データベース
# 3307 db-test:  テスト用データベース
```

デフォルトで使用されるポート番号は`8081`です。`-Dserver.port=XXXX`オプションを付けることでポート番号を変更できます。

```sh
# 1. run .jar file
# -Dspring.profiles.activeを指定しない場合はlocalになる
$ java -jar iam-<version>.jar  # -Dspring.profiles.active=<environment>

# 2. run on dev environment
$ ./gradlew bootRun
```

### DBマイグレーションとコード生成

DBマイグレーションとORMには下記ツールを利用しています。

* Flyway: DBマイグレーションツール
* MyBatis: ORMフレームワーク

#### DBマイグレーション

`flywayMigrate`タスクでDBマイグレーションを実行できます。

```sh
$ ./gradlew flywayMigrate
```

Flywayはマイグレーションファイルのチェックサムを`flyway_schema_history`テーブルに保存することで、過去のマイグレーションファイルが書き換えられることを防いでいます。

しかしながら、開発中は書き換えたくなることもあるでしょう。その場合は、下記コマンドで歴史を消すことが可能です。クリーンが完了したら、再度マイグレーションコマンドを実行してください。

```sh
$ ./gradlew flywayClean
```

#### MyBatisでコード生成

下記コマンドで、DBからEntityファイルを生成できます。

```sh
$ ./gradlew mbGenerate
```

### 依存関係のアップデート

[Gradle Versions Plugin](https://github.com/ben-manes/gradle-versions-plugin)を使って、outdatedな依存関係をアップデートします。

下記コマンドの実行後、出力されたレポートに従って[build.gradle](./app/build.gradle)に記載されたバージョンを書き換えてください。

```sh
$ ./gradlew dependencyUpdates -Drevision=release
