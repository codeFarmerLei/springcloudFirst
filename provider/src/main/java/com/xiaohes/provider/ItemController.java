package com.xiaohes.provider;

import com.alibaba.fescar.core.context.RootContext;
import com.xiaohes.common.bean.Result;
import com.xiaohes.provider.mapping.ItemMapper;
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
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @GetMapping("/getName")
    public String getName(){
        return "apple!";
    }


    @Autowired
    ItemMapper itemMapper;

    @GetMapping("/deduct")
    public String deduct(String commodityCode, int count) {
        log.info("Storage Service Begin ... xid: " + RootContext.getXID());
        log.info("Deducting inventory SQL: update storage_tbl set count = count - {} where commodity_code = {}",count,commodityCode);

        Map<String,Object> param = new HashMap<>();
        param.put("count",count);
        param.put("commodityCode",commodityCode);
        itemMapper.update(param);

        log.info("===============================Storage Service End ... ");
        return Result.success().toString();
    }
}
