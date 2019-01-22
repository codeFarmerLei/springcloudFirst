package com.xiaohes.feign.impl;

import com.xiaohes.common.bean.Result;
import com.xiaohes.feign.OrderService;
import org.springframework.stereotype.Component;

/**
 * @author by lei
 * @date 2019-1-22 13:41
 */
@Component
public class OrderFallback implements OrderService {
    @Override
    public Result create(String userId, String commodityCode, int orderCount) {
        return Result.error();
    }
}
