package com.xiaohes.provider.controller;

import com.xiaohes.common.bean.Result;
import com.xiaohes.common.bean.User;
import com.xiaohes.common.service.UserServiceDetail;
import com.xiaohes.feign.AuthServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 用户相关控制器
 * @author by lei
 * @date 2019-1-24 14:59
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceDetail userServiceDetail;

    @PostMapping("/register")
    public User postUser(@RequestParam("username") String username , @RequestParam("password") String password){
        //参数判断，省略
        return userServiceDetail.insert(username,password);
    }

    @PostMapping("/login")
    public Result login(@RequestParam("username") String username , @RequestParam("password") String password){
        //参数判断，省略
        try {
            return userServiceDetail.login(username,password);
        } catch (Exception e) {
            e.printStackTrace();

            return Result.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/foo", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getFoo() {
        return "i'm foo, " + UUID.randomUUID().toString();
    }

}
