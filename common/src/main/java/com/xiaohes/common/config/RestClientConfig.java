package com.xiaohes.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate注入
 *
 */
@Configuration
public class RestClientConfig
{
    @Bean
    public RestTemplate restTemplate() {
        //配置RestTemplate
        //使用OKHttpClient进行请求
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
