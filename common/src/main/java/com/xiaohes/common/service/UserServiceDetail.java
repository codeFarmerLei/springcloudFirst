package com.xiaohes.common.service;

import com.xiaohes.common.bean.Result;
import com.xiaohes.common.bean.User;
import com.xiaohes.common.mapping.RoleMapper;
import com.xiaohes.common.mapping.UserServiceDetailMapper;
import com.xiaohes.common.utils.BPwdEncoderUtil;
import com.xiaohes.feign.AuthServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by lei
 * @date 2019-1-24 12:33
 */
@Service
public class UserServiceDetail implements UserDetailsService {

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserServiceDetailMapper userServiceDetailMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userServiceDetailMapper.selectByUsername(s);
        if (user != null) {
            user.setAuthorities(roleMapper.selectByUserId(user.getId()));
        }
        return user;
    }

    /**
     * 添加用户
     * @param username
     * @param password
     * @return
     */
    public User insert(String username,String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(BPwdEncoderUtil.BCryptPassword(password));
        Long id = userServiceDetailMapper.insert(user);
        user.setId(id);
        return user;
    }



    @Autowired
    private AuthServiceFeign client;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public Result login(String username, String password) throws Exception {

        User user = userServiceDetailMapper.selectByUsername(username);
        if (null == user) {
            return Result.error("error username");
        }
        if(!BPwdEncoderUtil.matches(password,user.getPassword())){
            return Result.error("error password");
        }



        // 获取token
        Map<String,Object> jwt = client.getToken("Basic dXNlci1zZXJ2aWNlOjEyMzQ1Ng==","password",username,password);
        // 获得用户菜单
        if(jwt == null){
            return Result.error("getToken error");
        }

        Map<String,Object> data = new HashMap<>();
        data.put("jwt",jwt);
        data.put("user",user);



        return Result.success(data);

    }
}
