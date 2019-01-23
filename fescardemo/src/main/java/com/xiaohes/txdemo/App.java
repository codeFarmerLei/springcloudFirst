package com.xiaohes.txdemo;

import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.xiaohes.*"})
@EnableCircuitBreaker
public class App
{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
        return new GlobalTransactionScanner("fescardemo","my_test_tx_group");
    }
}
