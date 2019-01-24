package com.xiaohes.txdemo;

import com.alibaba.fescar.core.context.RootContext;
import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.xiaohes.common.bean.Result;
import com.xiaohes.feign.ProviderFeign;
import com.xiaohes.feign.DemoFeign;
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
    ProviderFeign providerFeign;
    @Autowired
    DemoFeign demoFeign;
    //@Autowired
    //private RedisUtil redisUtil;

    @GetMapping("/purchase")
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
    public void purchase(String userId, String commodityCode, int orderCount) {
        log.info("=========================begin=============================");
        String xid = RootContext.getXID();
        log.info("purchase begin ... xid: {},commodityCode:{},orderCount:{},userId:{}", xid ,commodityCode,orderCount,userId);

        //redisUtil.add(RootContext.KEY_XID, xid);
        String dret = providerFeign.deduct(commodityCode, orderCount);
        Result result = demoFeign.create(userId, commodityCode, orderCount);
        log.info(result.toString()+",,,,,,"+dret);
        log.info("=========================success=============================");
        //throw new RuntimeException("xxx");
    }


    @GetMapping("/hi")
    public String hi()
    {
        return "hi "+ demoFeign.gettx();
    }
}
