package com.ken.config;

import com.ken.realm.SecondShiroRealm;
import com.ken.realm.ShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfiguration {


    //@Bean
    //public EhCacheManager cacheManager() {
    //    EhCacheManager cacheManager = new EhCacheManager();
    //    cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
    //    return cacheManager;
    //}

    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        //shiroRealm.setCredentialsMatcher(credentialsMatcher());
        return shiroRealm;
    }

    @Bean
    public SecondShiroRealm secondShiroRealm() {
        SecondShiroRealm secondShiroRealm = new SecondShiroRealm();
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("SHA1");
        secondShiroRealm.setCredentialsMatcher(credentialsMatcher);
        return secondShiroRealm;
    }

    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        return credentialsMatcher;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //securityManager.setCacheManager(cacheManager());
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    @Bean
    public ModularRealmAuthenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        List<Realm> realmList = new ArrayList<>();
        realmList.add(shiroRealm());
        realmList.add(secondShiroRealm());
        authenticator.setRealms(realmList);
        //authenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
        return authenticator;
    }

    /**
     * 可以自动的调用配置在 Spring IOC 容器中 shiro bean 的声明周期方法
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * IOC 容器使用 shiro 的注解，但必须在配置了 LifecycleBeanPostProcessor 之后才能使用
     *
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        return autoProxyCreator;
    }

    /**
     * bean 的名称必须和 web 配置中的 filterName 一直
     * anon:可以被匿名访问
     * authc:必须认证（即登录）后才可以访问
     *
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        Map<String, String> map = new HashMap<>();
        map.put("/login.jsp", "anon");
        map.put("/login", "anon");
        map.put("/**", "authc");
        map.put("/logout", "logout");

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl("/login.jsp");
        factoryBean.setSuccessUrl("/success.jsp");
        factoryBean.setUnauthorizedUrl("/unauthorized.jsp");
        factoryBean.setFilterChainDefinitionMap(map);

        return factoryBean;
    }

}
