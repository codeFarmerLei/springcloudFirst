package com.xiaohes.authservice.config;

import com.xiaohes.common.service.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * 配置 Authorization Server
 * @author by lei
 * @date 2019-1-24 13:37
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    UserServiceDetail userServiceDetail;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() //将客户端的信息存储在内存中
                .withClient("user-service") //创建了一个Client为"user-service"的客户端
                .secret(bCryptPasswordEncoder.encode("123456"))
                .scopes("service") //客户端的域
                .authorizedGrantTypes("refresh_token", "password") //配置类验证类型为 refresh_token和password
                .accessTokenValiditySeconds(12*300) //5min过期
                .and()
                .withClient("demo-service")
                .secret(bCryptPasswordEncoder.encode("123456"))
                .scopes("service") //客户端的域
                .authorizedGrantTypes("refresh_token", "password") //配置类验证类型为 refresh_token和password
                .accessTokenValiditySeconds(12*300) //5min过期
        ;
        ////从jdbc查出oauth_client_details数据
        //clients.withClientDetails(clientDetails());
    }
//    /**
//     * 数据源
//     */
//    @Autowired
//    DataSource dataSource;
//
//    @Bean
//    public ClientDetailsService clientDetails() {
//        return new JdbcClientDetailsService(dataSource);
//    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtTokenEnhancer()).authenticationManager(authenticationManager).userDetailsService(userServiceDetail);
    }

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        //注意此处需要相应的jks文件
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("xiaohes-jwt.jks"), "xiaohes123".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("xiaohes-jwt"));
        return converter;
    }
}
