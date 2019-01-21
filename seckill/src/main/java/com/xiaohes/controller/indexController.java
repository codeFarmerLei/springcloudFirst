package com.xiaohes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author by lei
 * @date 2019-1-15 10:02
 */
@RequestMapping(value = "/")
@Controller
public class indexController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
