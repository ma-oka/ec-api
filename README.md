## ec-api
一般ユーザーが使う事を想定したアプリ用のAPIです。

## DockerDBの立ち上げ
```
docker run --rm -d \
    -p 15432:5432 \
    -v postgres-tmp:/var/lib/postgresql/data \
    postgres:11-alpine
```
##  コマンドラインからテーブルファイルの実行
```
psql -f master.sql -h localhost -p 15432 -U postgres

psql -f fixture.sql -h localhost -p 15432 -U postgres
```
