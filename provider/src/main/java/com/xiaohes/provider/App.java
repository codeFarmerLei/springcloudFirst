package com.xiaohes.provider;

import com.alibaba.fescar.rm.RMClientAT;
import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@EnableDiscoveryClient()
@SpringBootApplication
@EnableCircuitBreaker
@ComponentScan("com.xiaohes.*")
public class App 
{
    public static void main(String[] args) {

        String applicationId = "provider";
        String txServiceGroup = "my_test_tx_group";

        RMClientAT.init(applicationId, txServiceGroup);

        SpringApplication.run(App.class, args);
    }

    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
        return new GlobalTransactionScanner("provider","my_test_tx_group");
    }
}
