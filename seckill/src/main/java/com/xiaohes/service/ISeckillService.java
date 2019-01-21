package com.xiaohes.service;

import com.github.pagehelper.PageInfo;
import com.xiaohes.common.bean.Result;

import java.util.Map;

public interface ISeckillService {

	/**
	 * 查询全部的秒杀记录
	 * @return
	 */
    PageInfo<Map<String,Object>> getSeckillList();


	Result startSeckill(long seckillId, long userId);

	Result startSeckilRedisLock(long seckillId, long userId);

}
