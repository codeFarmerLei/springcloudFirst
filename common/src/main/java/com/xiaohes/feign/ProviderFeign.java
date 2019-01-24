package com.xiaohes.feign;

import com.xiaohes.common.bean.User;
import com.xiaohes.common.interceptor.FeignBasicAuthRequestInterceptor;
import com.xiaohes.feign.impl.ProviderFallback;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品feignclient
 * @author by lei
 * @date 2019-1-16 14:32
 */
@FeignClient(value = "provider",fallback = ProviderFallback.class,configuration = FeignBasicAuthRequestInterceptor.class)
@Component
public interface ProviderFeign {


    @LoadBalanced//启用负载均衡
    @GetMapping("/getName")
    String getName();

    @GetMapping("/deduct")
    String deduct(@RequestParam("commodityCode") String commodityCode,@RequestParam("count") int count);

    @PostMapping("/register")
    User postUser(@RequestParam("username") String username , @RequestParam("password") String password);

}
