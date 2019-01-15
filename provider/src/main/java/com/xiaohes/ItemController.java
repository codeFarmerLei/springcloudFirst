package com.xiaohes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by lei
 * @date 2019-1-14 19:13
 */
@RestController
public class ItemController {

    @GetMapping("/getName")
    public String getName(){
        return "apple!";
    }
}
