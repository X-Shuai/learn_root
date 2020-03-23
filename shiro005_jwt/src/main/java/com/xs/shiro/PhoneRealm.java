package com.xs.shiro;

import com.xs.dao.UserMapper;
import com.xs.pojo.User;
import com.xs.shiro.sms.PhoneToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-03-22 23:01
 **/
public class PhoneRealm extends AuthorizingRealm {
    @Resource
    UserMapper userMapper;
    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {


        PhoneToken token = null;

        // 如果是PhoneToken，则强转，获取phone；否则不处理。
        if(authenticationToken instanceof PhoneToken){
            token = (PhoneToken) authenticationToken;
        }else{
            return null;
        }

        String phone = (String) token.getPrincipal();

        User user = userMapper.findByUserName(phone);

        if (user == null) {
            throw new RuntimeException("手机号错误");
        }
        return new SimpleAuthenticationInfo(user, phone, this.getName());
    }

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken var1){
        return var1 instanceof PhoneToken;
    }

}
