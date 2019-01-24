package com.xiaohes.common.mapping;

import com.xiaohes.common.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author by lei
 * @date 2019-1-24 12:57
 */
@Mapper
public interface UserServiceDetailMapper {

    @Select("select * from auth_user where username=#{username} ")
    User selectByUsername(String username);

    @Insert("insert into auth_user(username,password) values(#{username},#{password})")
    Long insert(User user);
}
