package com.xiaohes.demo;

import com.alibaba.fescar.common.exception.ShouldNeverHappenException;
import com.alibaba.fescar.common.thread.NamedThreadFactory;
import com.alibaba.fescar.core.context.RootContext;
import com.alibaba.fescar.core.exception.TransactionException;
import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.alibaba.fescar.tm.api.FailureHandler;
import com.alibaba.fescar.tm.api.GlobalTransaction;
import com.alibaba.fescar.tm.api.TransactionalExecutor;
import com.alibaba.fescar.tm.api.TransactionalTemplate;
import com.xiaohes.common.annotation.Servicelock;
import com.xiaohes.common.annotation.TransBind;
import com.xiaohes.common.bean.Result;
import com.xiaohes.common.redis.RedisUtil;
import com.xiaohes.demo.mapping.OrderMapper;
import com.xiaohes.feign.ItemClient;
import com.xiaohes.feign.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
    public String gettx(){
        String xid = RootContext.getXID();
        log.info("==================================={}",xid);

        redisUtil.add(RootContext.KEY_XID, xid);
        log.info(redisUtil.get(RootContext.KEY_XID).toString());
        return xid;
    }

    @GetMapping("/create")
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


    @Autowired
    ItemClient itemClient;
    @Autowired
    OrderService orderService;
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/purchase")
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
    public void purchase(String userId, String commodityCode, int orderCount) {
        log.info("=========================begin=============================");
        String xid = RootContext.getXID();
        log.info("purchase begin ... xid: {},commodityCode:{},orderCount:{},userId:{}", xid ,commodityCode,orderCount,userId);

        redisUtil.add(RootContext.KEY_XID, xid);
        String dret = itemClient.deduct(commodityCode, orderCount);
        int f=1/0;
        Result result = orderService.create(userId, commodityCode, orderCount);
        log.info(result.toString()+",,,,,,"+dret);
        log.info("=========================success=============================");
        throw new RuntimeException("xxx");
    }
}
