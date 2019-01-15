package com.xiaohes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Hello world!
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
