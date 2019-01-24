package com.xiaohes.feign;

import com.xiaohes.common.interceptor.FeignBasicAuthRequestInterceptor;
import com.xiaohes.feign.impl.AuthServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * auth-service 服务远程调用
 * @author by lei
 * @date 2019-1-24 15:10
 */

@FeignClient(value = "authservice",fallback = AuthServiceFallback.class,configuration = FeignBasicAuthRequestInterceptor.class)
@Component
public interface AuthServiceFeign {

    @PostMapping(value = "/oauth/token")
    Map<String,Object> getToken(@RequestHeader(value = "Authorization") String authorization, @RequestParam("grant_type") String type,
                                @RequestParam("username") String username, @RequestParam("password") String password);
}
