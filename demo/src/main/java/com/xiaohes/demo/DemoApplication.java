package com.xiaohes.demo;

import com.alibaba.fescar.rm.RMClientAT;
import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;
import com.alibaba.fescar.tm.TMClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients("com.xiaohes.feign")
@ComponentScan("com.xiaohes.*")
//@ComponentScan(basePackages = {"com.xiaohes.feign","com.xiaohes.demo"})
@EnableCircuitBreaker
public class DemoApplication {

    public static void main(String[] args) {

        String applicationId = "demo";
        String txServiceGroup = "my_test_tx_group";

        RMClientAT.init(applicationId, txServiceGroup);

        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
        return new GlobalTransactionScanner("demo","my_test_tx_group");
    }
}

