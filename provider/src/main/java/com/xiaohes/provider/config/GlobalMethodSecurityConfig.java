package com.xiaohes.provider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * 开启方法级别的安全验证类
 * @author by lei
 * @date 2019-1-24 14:40
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class GlobalMethodSecurityConfig {
}
