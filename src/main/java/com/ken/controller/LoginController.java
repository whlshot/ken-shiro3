package com.ken.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public String login(String loginName, String password) {
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) { // 没有指定的账户
                uae.printStackTrace();
            } catch (IncorrectCredentialsException ice) {// 账户存在，密码不匹配
                ice.printStackTrace();
            } catch (LockedAccountException lae) {//用户被锁定
                lae.printStackTrace();
            } catch (AuthenticationException ae) {//所有认证时异常的父类
                ae.printStackTrace();
            }
        }
        return "success";
    }
}
