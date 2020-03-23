package com.xs.shiro;

import com.xs.dao.UserMapper;
import com.xs.dao.UserPermissionMapper;
import com.xs.dao.UserRoleMapper;
import com.xs.pojo.Permission;
import com.xs.pojo.Role;
import com.xs.pojo.User;
import com.xs.shiro.jwt.JWTToken;
import com.xs.shiro.jwt.JWTUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private UserPermissionMapper userPermissionMapper;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
	}

	/**
	 * 获取用户角色和权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		String username = JWTUtils.getUsername(principal.toString());
		User user =userMapper.findByUserName(username);
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addRole("admin");
		Set<String> permission = new HashSet<>(Arrays.asList("view,edit".split(",")));
		simpleAuthorizationInfo.addStringPermissions(permission);
		return simpleAuthorizationInfo;

	}

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		String token = (String) auth.getCredentials();
		// 解密获得username，用于和数据库进行对比
		String username = JWTUtils.getUsername(token);
		if (username == null) {
			throw new AuthenticationException("token invalid");
		}

		User userBean = userMapper.findByUserName(username);
		if (userBean == null) {
			throw new AuthenticationException("User didn't existed!");
		}

		if (! JWTUtils.verify(token, username, userBean.getPassword())) {
			throw new AuthenticationException("Username or password error");
		}

		return new SimpleAuthenticationInfo(token, token, "my_realm");
	}


}
