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


## 微信登录
### 基础配置
WxProperties.java
```java
@Data
@ConfigurationProperties(prefix = "xs.wx")
public class WxProperties {

	/**
	 * 小程序AppID
	 */
	private String maAppId;
	/**
	 * 小程序secret
	 */
	private String maAppSecret;

	/**
	 * 公众号AppID
	 */
	private String mpAppId;
	/**
	 * 公众号secret
	 */
	private String mpAppSecret;

	/**
	 * 授权回调地址
	 */
	private String authorizeUrl;

	/**
	 * 商户ID
	 */
	private String mchId;

	/**
	 * 商户key
	 */
	private String mchKey;

	/**
	 * 支付回调地址
	 */
	private String notifyUrl;

	/**
	 * 商户证书文件路径
	 */
	private String keyPath;
}

```

WxConfig.java
```java
public class WxConfig {
	private final WxProperties properties;

	public WxConfig(WxProperties properties) {
		this.properties = properties;
	}

	@Bean
	public WxMaConfig wxMaConfig() {
		WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
		config.setAppid(properties.getMaAppId());
		config.setSecret(properties.getMaAppSecret());
		return config;
	}

	@Bean
	public WxMpConfigStorage wxMpConfig() {
		WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
		config.setAppId(properties.getMpAppId());
		config.setSecret(properties.getMpAppSecret());
		return config;
	}

	@Bean
	public WxMaService wxMaService(WxMaConfig maConfig) {
		WxMaService service = new WxMaServiceImpl();
		service.setWxMaConfig(maConfig);
		return service;
	}

	@Bean
	public WxMpService wxMpService(WxMpConfigStorage mpConfigStorage) {
		WxMpService service = new WxMpServiceImpl();
		service.setWxMpConfigStorage(mpConfigStorage);
		return service;
	}

	@Bean
	public WxPayConfig wxPayConfig() {
		WxPayConfig payConfig = new WxPayConfig();
		payConfig.setAppId(properties.getMpAppId());
		payConfig.setMchId(properties.getMchId());
		payConfig.setMchKey(properties.getMchKey());
		payConfig.setNotifyUrl(properties.getNotifyUrl());
		payConfig.setKeyPath(properties.getKeyPath());
		payConfig.setTradeType("JSAPI");
		payConfig.setSignType("MD5");
		return payConfig;
	}

	@Bean
	public WxPayService wxPayService(WxPayConfig payConfig) {
		WxPayService wxPayService = new WxPayServiceImpl();
		wxPayService.setConfig(payConfig);
		return wxPayService;
	}

	@Bean
	public EntPayService entPayService(WxPayService wxPayService) {
		return new EntPayServiceImpl(wxPayService);
	}
}

```
相关配置对象
```java

@Data
public class WxLoginInfo {
    @NotBlank(message = "code不能为空")
    private String code;

    @NotBlank(message = "encryptedData不能为空")
    private String encryptedData;

    @NotBlank(message = "iv不能为空")
    private String iv;

    @NotBlank(message = "signature不能为空")
    private String signature;

    @NotNull(message = "userInfo不能不传")
    private UserInfo userInfo;
}

@Data
@Accessors
public class UserInfoVo {
    private String token;
    private UserInfo userInfo;
}
/***
*
*/
@Data
public class UserInfo {
    private Integer id;
    private String nickName;
    private String avatarUrl;
    private String country;
    private String province;
    private String city;
    private String language;
    private Integer gender;
    private String unionId;
    private String openId;
}
```
token配置
```java

public class WxAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	// ~ Instance fields
	// ================================================================================================

	private final Object principal;

	private  String openId;

	private  String unionId;
	// ~ Constructors
	// ===================================================================================================

	/**
	 * This constructor can be safely used by any code that wishes to create a
	 * <code>SmsCodeAuthenticationToken</code>, as the {@link #isAuthenticated()}
	 * will return <code>false</code>.
	 *
	 * @param openId 微信用户标识码
	 */
	public WxAuthenticationToken(String openId) {
		super(null);
		this.principal=null;
		this.openId = openId;
		setAuthenticated(false);
	}


	/****
	 * @param openId  微信用户openId
	 * @param unionId 微信用户unionId
	 */
	public WxAuthenticationToken(String openId,String unionId) {
		super(null);
		this.principal=null;
		this.openId = openId;
		this.unionId=unionId;
		setAuthenticated(false);
	}
	/**
	 * This constructor should only be used by <code>AuthenticationManager</code> or
	 * <code>AuthenticationProvider</code> implementations that are satisfied with
	 * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
	 * authentication token.
	 *
	 * @param principal   认证信息
	 * @param authorities 权限列表
	 */
	public WxAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	public String getOpenId() {
		return openId;
	}

	public String getUnionId() {
		return unionId;
	}
}

```
WxAuthenticationSecurityConfig
```java
@AllArgsConstructor
public class WxAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private final WxUserDetailsService userDetailsService;

	private final AuthenticationSuccessHandler authenticationSuccessHandler;

	private final AuthenticationFailureHandler authenticationFailureHandler;
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 首先配置Sms验证的过滤器，在其中配置AuthenticationManager，验证成功处理器、失败处理器和认证的Provider等信息
		WxAuthenticationProvider authenticationProvider = new WxAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		// 将Provider注册到Spring Security中，将Filter加到UsernamePasswordAuthenticationFilter后面
		http.authenticationProvider(authenticationProvider);
	}
}
```
WxAuthenticationProvider
```java
@Data
public class WxAuthenticationProvider implements AuthenticationProvider {

	private WxUserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// 将Authentication的对象强转为SmsCodeAuthenticationToken对象
		WxAuthenticationToken authenticationToken = (WxAuthenticationToken) authentication;
		// 根据微信用户ID载入用户信息
		UserDetails user = userDetailsService.loadUserByOpenId(authenticationToken.getOpenId());
		if (user == null) {
			throw new InternalAuthenticationServiceException("用户信息不存在");
		}
		// 将获取到的用户信息封装到SmsCodeAuthenticationToken第二个构造方法中，在这个方法中设置为"已认证"
		WxAuthenticationToken authenticationResult = new WxAuthenticationToken(user, user.getAuthorities());
		// 将用户的细节信息封装到已认证的token中
		authenticationResult.setDetails(authenticationToken.getDetails());
		return authenticationResult;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return WxAuthenticationToken.class.isAssignableFrom(authentication);
	}
}

```
实现接口
```java
public interface WxUserDetailsService {


	UserDetails loadUserByOpenId(String openId) throws UsernameNotFoundException;

	
	UserDetails loadUserByUnionId(String unionId) throws UsernameNotFoundException;
}
//实现方式

@Override
	public UserDetails loadUserByOpenId(String openId) throws UsernameNotFoundException {
//缓存
		//Cache cache = cacheManager.getCache("user_details");
		//if (cache != null && cache.get(openId) != null) {
		//	return (UserDetail) cache.get(openId).get();
		//}
		UserVo userVo = userService.findOneVoByWxOpenId(openId);
		if (userVo == null) {
			throw new UsernameNotFoundException("用户不存在");
		}
		//UserDetails userDetails = getUserDetails(userService.getUserInfo(userVo));
		//cache.put(openId, userDetails);
		return userDetails;
	}

	@Override
	public UserDetails loadUserByUnionId(String unionId) throws UsernameNotFoundException {
		return null;
	}
```
配置
```java
1. 
private final WxUserDetailsService wxUserDetailsService;
2.
private WxAuthenticationSecurityConfig wxConfigurer(){
		return  new WxAuthenticationSecurityConfig(wxUserDetailsService,ajaxAuthenticationSuccessHandler(), ajaxAuthenticationFailureHandler());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
    http.
        //.....
        .and()
         .apply(wxConfigurer());
    

       }
```
接口
```java
引入依赖
private WxMaService wxMaService;
	private UserService userService;
	private final CaptchaCodeService captchaCodeService;
	private AuthenticationManager authenticationManager;
	private TokenProvider tokenProvider;

@PostMapping("/authenticate")
	@ApiOperation(value = "微信登录")
	public R authenticate(@RequestBody WxLoginInfo wxLoginInfo) {
		try {
			WxMaJscode2SessionResult result = wxMaService.getUserService().getSessionInfo(wxLoginInfo.getCode());
			WxMaUserInfo wxMaUser = wxMaService.getUserService().getUserInfo(result.getSessionKey(), wxLoginInfo.getEncryptedData(), wxLoginInfo.getIv());
			log.debug("微信jsSessionResult>>>" + result.toString());
			log.debug(wxMaUser.toString());
			if (result.getSessionKey() == null) {
				throw new RuntimeMsgException("sessionKey为空");
			}
			if (wxMaUser.getOpenId() == null) {
				throw new RuntimeMsgException("openId is null");
			}
			WxAuthenticationToken authenticationToken = new WxAuthenticationToken(wxMaUser.getOpenId());
			try {
				Authentication authenticate = authenticationManager.authenticate(authenticationToken);
				String jwt = tokenProvider.createToken(authenticate, true);
				log.info("微信授权登录>>>jwt:{}", jwt);
				return R.buildOkData(new LinkedHashMap<String, Object>() {{
					put("access_token", jwt);
					put("expires_in", tokenProvider.getExpirationDateSecondsFromToken(jwt));
				}});
			} catch (UsernameNotFoundException e) {
				return R.buildFailData(new LinkedHashMap<String, Object>() {{
					put("openid",wxMaUser.getOpenId());
					put("message","账户为绑定账号" );
				}});
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.buildFail("微信授权异常");
		}
	}

```










