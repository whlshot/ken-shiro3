package com.ken.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class SecondShiroRealm extends AuthenticatingRealm {

    private final static Logger logger = LoggerFactory.getLogger(SecondShiroRealm.class);

    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String loginName = upToken.getUsername();

        //根据登录名，从数据库中获取数据
        logger.info("loginName is: {}", loginName);
        if ("unknown".equals(loginName)) {
            throw new UnknownAccountException("用户不存在");
        }

        if ("locked".equals(loginName)) {
            throw new LockedAccountException("用户被锁定");
        }

        //1) principal: 认证的实体信息，可以是数据库表中对应得用户实体对象
        Object principal = loginName;

        //2) credentials: 密码
        Object credentials = "123456";
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, getName());

        return info;
    }

}
