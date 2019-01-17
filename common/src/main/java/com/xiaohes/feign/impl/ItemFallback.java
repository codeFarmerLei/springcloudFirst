package com.xiaohes.feign.impl;

import com.xiaohes.feign.ItemClient;
import org.springframework.stereotype.Component;

/**
 * 商品服务熔断降级实现
 * @author by lei
 * @date 2019-1-17 9:34
 */
@Component
public class ItemFallback implements ItemClient {
    @Override
    public String getName() {
        return "item server error!";
    }
}
