package com.xiaohes.mapping;

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
public interface SeckillMapper {

    @Select("select * from seckill")
    List<Map<String,Object>> selectAll();


    //校验库存
    @Select("SELECT number FROM seckill WHERE seckill_id=#{id}")
    Long selectItemNum(long id);


    //扣库存
    @Update("UPDATE seckill  SET number=number-1 WHERE seckill_id=#{id} and number>0")
    void updateItemNum(long id);
}
