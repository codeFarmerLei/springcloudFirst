package com.xiaohes.common.mapping;

import com.xiaohes.common.bean.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author by lei
 * @date 2019-1-24 12:57
 */
@Mapper
public interface RoleMapper {

    @Select("select * from auth_role where id in(select role_id from user_role where user_id=#{userid}) ")
    List<Role> selectByUserId(Long userid);

}
