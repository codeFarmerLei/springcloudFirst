package com.xiaohes.provider.config;

import com.xiaohes.oauth.MyAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
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
                .antMatchers("/user/login","/user/register","/user/authtest").permitAll()
                .antMatchers("/**").authenticated();

    }

    @Autowired
    TokenStore tokenStore;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenExtractor tokenExtractor;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .tokenStore(tokenStore)
                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .authenticationManager(authenticationManager)
                .tokenExtractor(tokenExtractor)
        ;
    }

    @Bean
    public AuthenticationManager oAuth2AuthenticationManager(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);

        OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
        authenticationManager.setTokenServices(tokenServices);
        return authenticationManager;
    }

    @Bean
    public TokenExtractor bearerTokenExtractor(){
        return new BearerTokenExtractor();
    }


    //@Autowired
    //private OAuth2ClientProperties oAuth2ClientProperties;

    //@Bean
    //public BaseOAuth2ProtectedResourceDetails baseOAuth2ProtectedResourceDetails(){
    //    BaseOAuth2ProtectedResourceDetails rd = new BaseOAuth2ProtectedResourceDetails();
    //    return rd;
    //}
}
