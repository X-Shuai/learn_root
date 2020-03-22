# oath2快速搭建
快速实现继承WebSecurityConfigurerAdapter,添加EnableAuthorizationServer注解

```java

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
在yml中配置client_id和client_secret
```yaml
security:
  oauth2:
    client:
      client-id: xs
      client-secret: shuai
      registered-redirect-uri: http://xs-shuai.com
```
再配置user实体,userDetailService

在请求中带上client-id和重定向的地址
如
```html
http://localhost:8080/oauth/authorize?
response_type=code&
client_id=xs&
redirect_uri=http://xs-shuai.com&
scope=all&
state=xs
```
注释
response_type:授权模式 code为授权码模式
client_id: 配置的client_id
redirect_uri:重定向的地址
scope:获取的权限 all
state:授权后会原样的跳转到服务器








