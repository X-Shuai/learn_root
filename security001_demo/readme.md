# 基础的登录
基本步骤

1. 引入入依赖
2. user实体
3. 密码配置  passwordEncoder
4. UserDetailService 配置 loadByUsername
5. 配置 成功和失败的handler
6. 配置config
```java
@Configuration
   public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
       @Autowired
       private MyAuthenticationSucessHandler authenticationSucessHandler;
   
       @Autowired
       private MyAuthenticationFailureHandler authenticationFailureHandler;
   
   
   
       @Override
       protected void configure(HttpSecurity http) throws Exception {
           http.formLogin() // 表单登录
                   // http.httpBasic() // HTTP Basic
                   .loginPage("/authentication/require") // 登录跳转 URL
                   .loginProcessingUrl("/login") // 处理表单登录 URL
                   .successHandler(authenticationSucessHandler) // 处理登录成功
                   .failureHandler(authenticationFailureHandler) // 处理登录失败
                   .and()
                   .authorizeRequests() // 授权配置
                   .antMatchers("/authentication/require", "/login.html").permitAll() // 登录跳转 URL 无需认证
                   .anyRequest()  // 所有请求
                   .authenticated() // 都需要认证
                   .and().csrf().disable();
       }
   }
```
