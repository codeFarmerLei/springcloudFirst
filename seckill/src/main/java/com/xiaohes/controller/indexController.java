package com.xiaohes.controller;

import com.xiaohes.common.annotation.Servicelock;
import com.xiaohes.common.bean.Result;
import com.xiaohes.mapping.SeckillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author by lei
 * @date 2019-1-15 10:02
 */
@RequestMapping(value = "/")
@Controller
public class indexController {

    @Autowired
    SeckillMapper seckillMapper;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/update")
    @Servicelock
    public String update() {

        try {
            Thread.sleep(5000);
            //扣库存
            seckillMapper.updateItemNum(1003);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index";
    }



}
