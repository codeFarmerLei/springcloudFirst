package com.xiaohes.authservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by lei
 * @date 2019-1-25 11:46
 */
@RestController
public class IndexController {

    @GetMapping("/index")
    public String index(){
        return "index";
    }
}
