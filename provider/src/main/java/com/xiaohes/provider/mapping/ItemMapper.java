package com.xiaohes.provider.mapping;

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
public interface ItemMapper {

    @Insert("update storage_tbl set count = count - #{count} where commodity_code=#{commodityCode}")
    Integer update(Map<String, Object> param);
}
