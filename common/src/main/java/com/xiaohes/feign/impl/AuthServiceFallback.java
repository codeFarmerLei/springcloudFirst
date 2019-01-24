package com.xiaohes.feign.impl;

import com.xiaohes.feign.AuthServiceFeign;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author by lei
 * @date 2019-1-24 15:14
 */
@Component
public class AuthServiceFallback implements AuthServiceFeign {
    @Override
    public Map<String,Object> getToken(String authorization, String type, String username, String password) {
        return null;
    }
}
