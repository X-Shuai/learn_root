# spring cloud albaba

父级项目的依赖
```xml
<!--统一管理jar包版本-->
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <junit.version>4.12</junit.version>
        <log4j.version>1.2.17</log4j.version>
        <lombok.version>1.16.18</lombok.version>
        <mysql.version>8.0.18</mysql.version>
        <druid.version>1.1.16</druid.version>
        <druid.spring.boot.starter.version>1.1.10</druid.spring.boot.starter.version>
        <spring.boot.version>2.2.2.RELEASE</spring.boot.version>
        <spring.cloud.version>Hoxton.SR1</spring.cloud.version>
        <spring.cloud.alibaba.version>2.1.0.RELEASE</spring.cloud.alibaba.version>
        <mybatis.spring.boot.version>1.3.0</mybatis.spring.boot.version>
        <mybatis-spring-boot-starter.version>2.1.1</mybatis-spring-boot-starter.version>
        <hutool-all.version>5.1.0</hutool-all.version>
    </properties>

    <!--子模块继承后,提供作用:锁定版本+子module不用groupId和version-->
    <dependencyManagement>
        <dependencies>
            <!--springboot 2.2.2-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring.boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--Spring cloud Hoxton.SR1-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring.cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--Spring cloud alibaba 2.1.0.RELEASE-->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring.cloud.alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>${druid.version}</version>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid-spring-boot-starter</artifactId>
                <version>${druid.spring.boot.starter.version}</version>
            </dependency>
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${mybatis-spring-boot-starter.version}</version>
            </dependency>
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

<!--    <build>-->
<!--        <plugins>-->
<!--            <plugin>-->
<!--                <groupId>org.springframework.boot</groupId>-->
<!--                <artifactId>spring-boot-maven-plugin</artifactId>-->
<!--                <configuration>-->
<!--                    <fork>true</fork>-->
<!--                    <addResources>true</addResources>-->
<!--                </configuration>-->
<!--            </plugin>-->
<!--        </plugins>-->
<!--    </build>-->

    <!--第三方maven私服-->
    <repositories>
        <repository>
            <id>nexus-aliyun</id>
            <name>Nexus aliyun</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

```
## Nacos
### 入门
1. 安装nacos 
```shell script
docker run --env MODE=standalone --name nacos -d -p 8848:8848 nacos/nacos-server
```
2. 搭建项目注入:
客户端:
pom.xml 
```xml
<dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
yaml
```yaml
server:
  port: 83
spring:
  application:
    name: nacos-order-consumer
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.0.109:8848

#消费者将要去访问的微服务名称（注册成功进nacos的微服务提供者）
service-url:
  nacos-user-service: http://nacos-payment-provider
```
Controller和配置
```java
@RestController
@Slf4j
public class OrderController {

    @Resource
    private RestTemplate restTemplate;
    /***
    **调用服务
    */
    @Value("${service-url.nacos-user-service}")
    private String serverUrl;

    @GetMapping("/consumer/payment/nacos/{id}")
    public String paymentInfo(@PathVariable("id") Integer id){
        return restTemplate.getForObject(serverUrl + "/payment/nacos/" + id, String.class);
    }
}
    /***
    **
    **/

@Configuration
public class NacosConfig {
    @Bean
    @LoadBalanced //轮训 负载均很
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```
启动类
```java
@SpringBootApplication
@EnableDiscoveryClient
public class OrderNacos83 {
    public static void main(String[] args) {
        SpringApplication.run(OrderNacos83.class,args);
    }
}
```
3. 服务方
pom
```xml
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
yaml
```yaml
server:
  port: 9001

spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.0.109:8848

management:
  endpoints:
    web:
      exposure:
        include: "*"
```
Controller
```java
@RestController
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/nacos/{id}")
    public String getPayment(@PathVariable("id") Integer id){
        return "nacos register, serverport=" + serverPort + "\t id:" + id;
    }
}
```

启动类
```java

@SpringBootApplication
@EnableDiscoveryClient
public class Nacos9001 {

    public static void main(String[] args) {
        SpringApplication.run(Nacos9001.class,args);
    }
}
```
cp和ap的切换
//todo

### 配置中心

nacos 的配置中心进行配置
配置流程:
pom.xml
```xml
 <dependency>
   <groupId>com.alibaba.cloud</groupId>
   <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```
配置文件:bootrap 高于application
bootstrap.yml
```yaml

server:
  port: 3377
spring:
  application:
    name: nacos-config-client
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.0.109:8848 # 注册中心
      config:
        server-addr: 192.168.0.109:8848 # 配置中心
        file-extension: yml # 这里指定的文件格式需要和nacos上新建的配置文件后缀相同，否则读不到
```
application.yml
```yaml
spring:
  profiles:
    active: dev # 开发环境

```
配置规则:
${prefix} - ${spring.profiles.active} . ${file-extension}
prefix默认为pring.application.name的值，也可以通过配置项spring.cloud.nacos.config.prefix来配置；

spring.profiles.active即为当前环境对应的profile。注意，当spring.profiles.active为空时，对应的连接符-也将不存在，dataId的拼接格式变成${prefix}.${file-extension}；

file-extension为配置内容的数据格式，可以通过配置项spring.cloud.nacos.config.file-extension来配置。

分类配置:
多环境.多项目
三种配置概念:
namespace: 保留空间(public)   可用于生产环境:dev test prod
groupId: DEFAULT_GROUP 不同的服务分组(可进行分组)
dataId:  就是nacos配置的环境

配置:
1. 修改application.yaml active就可以进行修改(用于环境的切换)
2. 创建两个分组,在bootstrap.yaml添加分组, 添加分组 group:{group}
3. 创建命名空间,bootstrap中添加namespace: {namespace的id}

nacos:集群的配置
三个以上的nacos才能成为集群,
linux集群:
```shell script
1. 下载源文件,解压
2. 安装数据库,执行nacos-mysql.sql

```

docker 的安装集群:
nacos自带嵌入式数据库,

## sentinel
**安装:**
docker安装
```shell script
>docker pull bladex/sentinel-dashboard
>docker run --name sentinel -d -p 8858:8858 -d bladex/sentinel-dashboard
```
sentinel
资源:(代码,方法,被保护的对象)
规则:降级,熔断的规则

Rt:响应时间,

流量控制:
	调用关系控制,
	运行的指标,QPS,线程池,系统负载
	控制效果,
降级
	熔断降级的概念的hytrix不同


**流控规则:**
资源名:唯一的名称,默认的请求路径
- 阈值类型/单机阈值
 - QPS:每秒钟的请求数量
 - 线程数,调用该API的线程数达到一定的时候,进行限流
- 是否集群:
- 流控模式
 - 直接:直接断开
 - 关联:当A关联资源B的时候,B达到阈值,限制A
 - 链路
- 流控效果
 - 快速失败:快速失败
 - warm up:预热 慢慢启动
 - 排队时间
**降级规则:**
- 熔断降级(没有半开的状态)

**热点限流:**
//todo

**系统规则:**

**注解()**

**服务熔断**

**规则持久化**


## 分布式事务 
一个业务需要多个调用,多个链路
Seata 微服务分布式事务处理,
- 分布式事务处理过程-ID+三组件模型
 - Transaction ID(XID) 全局唯一的事务id
 - 三组件概念
    - Transaction Coordinator(TC):事务协调器,维护全局事务的运行状态,负责协调并驱动全局事务的提交或回滚
    - Transaction Manager(TM):控制全局事务的边界,负责开启一个全局事务,并最终发起全局提交或全局回滚的决议
    - Resource Manager(RM): 控制分支事务,负责分支注册、状态汇报,并接受事务协调的指令,驱动分支(本地)事务的提交和回滚
注解:@Transaction @GlobalTransactional
**安装过程:**
1.  docker run --name seata-server -p 8091:8091 seataio/seata-server:latest





    


 

 