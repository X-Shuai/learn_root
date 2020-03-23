package com.xs.config;

import com.xs.shiro.PhoneRealm;
import com.xs.shiro.ShiroRealm;
import com.xs.shiro.jwt.JWTFilter;
import org.apache.shiro.authc.AbstractAuthenticator;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {
	
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		// 添加自己的过滤器并且取名为jwt
		Map<String, Filter> filterMap = new HashMap<>();
		filterMap.put("jwt", new JWTFilter());
		shiroFilterFactoryBean.setFilters(filterMap);


		shiroFilterFactoryBean.setUnauthorizedUrl("/401");

		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setLoginUrl("/login");
		shiroFilterFactoryBean.setSuccessUrl("/index");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		
		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/plogin", "anon");

		filterChainDefinitionMap.put("/logout", "logout");
		filterChainDefinitionMap.put("/", "anon");
		filterChainDefinitionMap.put("/**", "authc");
		
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		
		return shiroFilterFactoryBean;
	}
	@Bean
	public PhoneRealm phoneRealm(){
		PhoneRealm phoneRealm = new PhoneRealm();
		return phoneRealm;
	}
	@Bean
	public ShiroRealm shiroRealm(){
		ShiroRealm shiroRealm = new ShiroRealm();
		return shiroRealm;
	}

	/**
	 * 认证器
	 */
	@Bean
	public AbstractAuthenticator abstractAuthenticator(ShiroRealm shiroRealm, PhoneRealm phoneRealm){
		// 自定义模块化认证器，用于解决多realm抛出异常问题
		ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator ();
		// 认证策略：AtLeastOneSuccessfulStrategy(默认)，AllSuccessfulStrategy，FirstSuccessfulStrategy
		authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
		// 加入realms
		List<Realm> realms = new ArrayList<>();
		realms.add(shiroRealm);
		realms.add(phoneRealm);
		authenticator.setRealms(realms);
		return authenticator;
	}


	@Bean
	public SecurityManager securityManager(ShiroRealm shiroRealm, PhoneRealm phoneRealm, AbstractAuthenticator abstractAuthenticator) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realms
		List<Realm> realms = new ArrayList<>();
		realms.add(shiroRealm);
		realms.add(phoneRealm);
		securityManager.setRealms(realms);
		// 自定义缓存实现，可以使用redis
//		securityManager.setCacheManager(shiroCacheManager());
		// 自定义session管理，可以使用redis
//		securityManager.setSessionManager(sessionManager());
		// 注入记住我管理器
		securityManager.setRememberMeManager(rememberMeManager());
		// 认证器
		securityManager.setAuthenticator(abstractAuthenticator);
		return securityManager;
	}

	/**
	 * cookie对象
	 * @return
	 */
	public SimpleCookie rememberMeCookie() {
		// 设置cookie名称，对应login.html页面的<input type="checkbox" name="rememberMe"/>
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		// 设置cookie的过期时间，单位为秒，这里为一天
		cookie.setMaxAge(86400);
		return cookie;
	}

	/**
	 * cookie管理对象
	 * @return
	 */
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		// rememberMe cookie加密的密钥
		cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
		return cookieRememberMeManager;
	}

 
//	@Bean
//    public SecurityManager securityManager(){
//       DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
//       securityManager.setRealm(shiroRealm());
//       securityManager.setRememberMeManager(rememberMeManager());
//       //设置缓存
////       securityManager.setCacheManager();
//       return securityManager;
//    }
	


	
}
