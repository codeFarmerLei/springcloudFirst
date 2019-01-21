package com.xiaohes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */

//@EnableDiscoveryClient
@EnableEurekaServer
@SpringBootApplication
@ComponentScan("com.xiaohes.*")
//@EnableFeignClients("com.xiaohes.feign")
//@EnableCircuitBreaker
public class App 
{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
