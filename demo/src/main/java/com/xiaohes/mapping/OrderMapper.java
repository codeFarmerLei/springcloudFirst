package com.xiaohes.mapping;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author by lei
 * @date 2019-1-17 17:55
 */
@Mapper
@Component
public interface OrderMapper {

    @Insert("insert into order_tbl(user_id, commodity_code, count, money) values(#{userId},#{commodityCode},#{count},#{money})")
    Integer Insert(Map<String, Object> item);
}
