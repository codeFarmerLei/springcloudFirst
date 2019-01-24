package com.xiaohes.feign.impl;

import com.xiaohes.common.bean.Result;
import com.xiaohes.feign.DemoFeign;
import org.springframework.stereotype.Component;

/**
 * @author by lei
 * @date 2019-1-22 13:41
 */
@Component
public class DemoFallback implements DemoFeign {

    @Override
    public String gettx() {
        return "gettx error";
    }

    @Override
    public Result create(String userId, String commodityCode, int orderCount) {
        return Result.error();
    }
}
