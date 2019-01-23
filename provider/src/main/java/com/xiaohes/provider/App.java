package com.xiaohes.provider;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.inject.Named;
import javax.sql.DataSource;

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
