package com.xiaohes.demo;

import com.alibaba.fescar.core.context.RootContext;
import com.xiaohes.common.annotation.TransBind;
import com.xiaohes.common.bean.Result;
import com.xiaohes.demo.mapping.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by lei
 * @date 2019-1-14 19:13
 */
@RestController
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderMapper orderMapper;

    @GetMapping("/gettx")
    public String gettx(){
        String xid = RootContext.getXID();
        log.info("==================================={}",xid);
        return xid;
    }

    @GetMapping("/create")
    @TransBind
    public Result create(String userId, String commodityCode, int orderCount) {
        log.info("Order Service Begin ... xid: " + RootContext.getXID());

        // 计算订单金额
        int orderMoney = 200 * orderCount;

        Map<String,Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("commodityCode", commodityCode);
        param.put("count", orderCount);
        param.put("money", orderMoney);

        orderMapper.Insert(param);

        log.info("===============================Order Service End ... Created ");

        return Result.success();
    }


}
