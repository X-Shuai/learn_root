# 中间件
[toc]

## activeMQ

## RabbitMQ
### 安装
1. 普通安装
2. docker安装
```shell script

docker run --name rabbitmq -d -p 15672:15672 -p 5672:5672 [镜像]

默认账号: guest guest 
```
角色:
1. none
2. impersonator
3. management
4. policymarker
5. monitoring
6. Admin

使用:
````java

````

**协议**
AMQP:  



RabbitMQ的问题
```java
//todo
1. AMQP结构
2. 生成者流转过程
```

转发规则
exchange:  
fanout: 全部转发到queue
direct:
topic:
headers: 

临时队列 

**核心概念**

