# elasticSearch
安装
```shell script
> docker run -d --name es -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.3.1
> docker exec -it es /bin/bash

#修改配置
> vi config/elasticsearch.yml
# 加入跨域配置
http.cors.enabled: true
http.cors.allow-origin: "*"
```
安装管理工具

## 基本概念

## 相关API

## 查询 