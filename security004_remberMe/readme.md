# Security记住我
```js
// 不使用数据库持久化会出现什么情况??
```
基于图像验证码的实现

持久化配置数据
连接数据源
```yaml
//数据源配置
spring:
  datasource:
    url: jdbc:mysql://192.168.0.109:3307/security_learn?useUnicode=yes&characterEncoding=UTF-8&useSSL=false
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
```
持久化的配置需要在配置中加入数据员的配置
在链接的数据源中需要创建如下表
```sql
CREATE TABLE persistent_logins (
    username VARCHAR (64) NOT NULL,
    series VARCHAR (64) PRIMARY KEY,
    token VARCHAR (64) NOT NULL,
    last_used TIMESTAMP NOT NULL
)
```
在页面中添加记住我的勾选框 name一定是 remember-me
```html
<input type="checkbox" name="remember-me"/> 记住我

```
## 配置类添加配置
```java
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAuthenticationSucessHandler authenticationSucessHandler;

    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailService userDetailService;


    /**
    PersistentTokenRepository为一个接口类，
    这里我们用的是数据库持久化，所以实例用的是PersistentTokenRepository的实现类JdbcTokenRepositoryImpl。
    JdbcTokenRepositoryImpl需要指定数据源，所以我们将配置好的数据源对象DataSource
     注入进来并配置到JdbcTokenRepositoryImpl的dataSource属性中。
    createTableOnStartup属性用于是否启动项目时创建保存token信息的数据表，这里设置为false，我们自己手动创建
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //密码验证前加入
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin() // 表单登录
                // http.httpBasic() // HTTP Basic
                .loginPage("/login.html") // 登录跳转 URL
                .loginProcessingUrl("/login") // 处理表单登录 URL
                .successHandler(authenticationSucessHandler) // 处理登录成功
                .failureHandler(authenticationFailureHandler) // 处理登录失败
                .and()
                //>>>>>>>>记住我的配置<<<<
                .rememberMe()
                .tokenRepository(persistentTokenRepository()) // 配置 token 持久化仓库
                .tokenValiditySeconds(3600) // remember 过期时间，单为秒
                .userDetailsService(userDetailService) // 处理自动登录逻辑
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/authentication/require","/code/image", "/login.html").permitAll() // 登录跳转 URL 无需认证
                .anyRequest()  // 所有请求
                .authenticated() // 都需要认证
                .and().csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```








