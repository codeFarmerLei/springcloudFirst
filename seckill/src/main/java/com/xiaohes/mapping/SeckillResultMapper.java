package com.xiaohes.mapping;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author by lei
 * @date 2019-1-17 17:55
 */
@Mapper
@Component
public interface SeckillResultMapper {

    @Insert("insert into success_killed values(#{seckillId},#{userId},#{state},now())")
    Integer Insert(Map<String,Object> item);


}
