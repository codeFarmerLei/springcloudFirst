package com.xiaohes.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by lei
 * @date 2019-1-15 10:02
 */
@RestController
@RequestMapping("/")
public class indexController {
    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
