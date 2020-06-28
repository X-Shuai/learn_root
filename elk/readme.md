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
**索引:** 分片的集合
**文档:** 数据中的一行数据
**元数据:** _index _type _id




## 相关API
1. 创建索引
```json5
 //put   /{索引}
{
 "settings": {
   "index": {
    "number_of_shards": "2", //分片数
    "number_of_replicas": "0" //副本数
  }
 }
}
//删除索引
//DELETE /{索引}

```
2. 添加数据
```json5
 // POST   /{索引}/{类型}/{id}
 // POST   /{索引}/{类型}/  //不指定id
//数据
{
 "id":1001,
 "name":"张三",
 "age":20,
 "sex":"男"
}

```
更新数据
```json5
// PUT   /{索引}/{类型}/{id}
//文档数据是不为修改的，但是可以通过覆盖的方式进行更新。
{
 "id":1001,
 "name":"张三",
 "age":21,
 "sex":"女"
}
```
```json5

//POST /{索引}/{类型}/{id}/_update
{
"doc":{
 "age":23
}
}
```
3. 删除数据
```json5
//DELETE  /{索引}/{类型}/{id}
//如果删除一条不存在的数据，会响应404：
```

## 查询 
1. 通过id查找
```json5
//GET /{索引}/{类型}/{id}
```
2. 查询全部数据
```json5
// GET  /{索引}/{类型}/_search
// GET  /{索引}/{类型}/_search?q=age:20
```
3. 批量查询
```json5
{"ids":[{id列表}]}

```
4. _bulk操作
```json5
// POST  /{索引}/{类型}/_bulk
{ action: { metadata }}\n
{ request body    }\n
{ action: { metadata }}\n
{ request body    }\n
```
5. DSL查询
```json5
// POST  /{索引}/{类型}/_search
{"query":{"match":{"字段":"值"}}}

```
高亮展示

```json5
{"query":{"match":{"字段":"值"}},highlight: {fields: {"字段": "值"}}}
```
聚合
```json5
{
  "aggs": {
    "all_interests": {
      "terms": {
        "field": "聚合字段"
     }
   }
 }
}
```

6. 分页
```json5
// GET  /{索引}/{类型}/_search?size={size}
// GET  /{索引}/{类型}/_search?size={size}&from={current}
// GET  /{索引}/{类型}/_search?size={size}&from={current}

```
7. 映射
```json5

```

### 结构化查询
8. 



>美化 加上个  ?pretty
>单独数据 ?_sources= {字段1},{字段2},...
>     /_sources 原始数据
>文档是否存在 HEAD请求    /{索引}/{类型}/{id} 判断状态码