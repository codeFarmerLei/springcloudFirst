package com.xiaohes.feign.impl;

import com.xiaohes.common.bean.User;
import com.xiaohes.feign.ProviderFeign;
import org.springframework.stereotype.Component;

/**
 * 商品服务熔断降级实现
 * @author by lei
 * @date 2019-1-17 9:34
 */
@Component
public class ProviderFallback implements ProviderFeign {
    @Override
    public String getName() {
        return "item server error!";
    }

    @Override
    public String deduct(String commodityCode, int count) {
        return "deduct error";
    }

    @Override
    public User postUser(String username, String password) {
        return null;
    }
}
