package com.xiaohes.provider.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 配置 Resource Server
 * @author by lei
 * @date 2019-1-24 14:38
 */
@Configuration
@EnableResourceServer //开启Resource Server功能
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/login","/user/register").permitAll()
                .antMatchers("/**").authenticated();

    }

    //@Autowired
    //TokenStore tokenStore;
    //
    //@Override
    //public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    //    resources.tokenStore(tokenStore);
    //}
}
