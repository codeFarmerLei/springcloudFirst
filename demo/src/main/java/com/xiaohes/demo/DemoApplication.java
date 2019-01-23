package com.xiaohes.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.fescar.rm.RMClientAT;
import com.alibaba.fescar.rm.datasource.DataSourceProxy;
import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients("com.xiaohes.feign")
@ComponentScan("com.xiaohes.*")
//@ComponentScan(basePackages = {"com.xiaohes.feign","com.xiaohes.demo"})
@EnableCircuitBreaker
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    //@Bean
    //public GlobalTransactionScanner globalTransactionScanner() {
    //
    //    String applicationId = "demo";
    //    String txServiceGroup = "my_test_tx_group";
    //    RMClientAT.init(applicationId, txServiceGroup);
    //
    //    return new GlobalTransactionScanner("demo","my_test_tx_group");
    //}

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDateSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("getDateSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        DataSourceProxy dspy = new DataSourceProxy((DruidDataSource) dataSource);
        bean.setDataSource(dspy);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate getSqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
        SqlSessionTemplate temp = new SqlSessionTemplate(sqlSessionFactory);
        return temp;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}

