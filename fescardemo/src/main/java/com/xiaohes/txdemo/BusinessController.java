package com.xiaohes.txdemo;

import com.alibaba.fescar.core.context.RootContext;
import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.xiaohes.common.bean.Result;
import com.xiaohes.feign.ItemClient;
import com.xiaohes.feign.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by lei
 * @date 2019-1-14 19:13
 */
@RestController
public class BusinessController {
    private static final Logger log = LoggerFactory.getLogger(BusinessController.class);

    @Autowired
    ItemClient itemClient;
    @Autowired
    OrderService orderService;

    @GetMapping("/purchase")
    //@GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
    public void purchase(String userId, String commodityCode, int orderCount) {
        log.info("purchase begin ... xid: {},commodityCode:{},orderCount:{},userId:{}",  RootContext.getXID(),commodityCode,orderCount,userId);
        String dret = itemClient.deduct(commodityCode, orderCount);
        Result result = orderService.create(userId, commodityCode, orderCount);
        //throw new RuntimeException("xxx");
        log.info(result.toString()+",,,,,,"+dret);
        log.info("=========================success=============================");
    }

    @GetMapping("/index")
    public String index()
    {
        return "index";
    }

    @GetMapping("/hi")
    public String hi()
    {
        return "hi "+itemClient.getName();
    }
}