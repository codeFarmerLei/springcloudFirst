package com.xiaohes.feign;

import com.xiaohes.common.bean.Result;
import com.xiaohes.feign.impl.OrderFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 订单服务
 * @author by lei
 * @date 2019-1-22 10:10
 */
@FeignClient(value = "demo",fallback = OrderFallback.class)
@Component
public interface OrderService {

    @GetMapping("/create")
    Result create(@RequestParam("userId") String userId,@RequestParam("commodityCode")  String commodityCode,@RequestParam("orderCount")  int orderCount);
}
