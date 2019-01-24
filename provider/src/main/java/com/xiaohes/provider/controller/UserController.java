package com.xiaohes.provider.controller;

import com.xiaohes.common.bean.Result;
import com.xiaohes.common.bean.User;
import com.xiaohes.common.service.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
