package com.xiaohes.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaohes.common.annotation.Servicelock;
import com.xiaohes.common.bean.Result;
import com.xiaohes.common.utils.RedissLockUtil;
import com.xiaohes.mapping.SeckillMapper;
import com.xiaohes.mapping.SeckillResultMapper;
import com.xiaohes.service.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service("seckillService")
public class SeckillServiceImpl implements ISeckillService {

	private final static Logger log = LoggerFactory.getLogger(SeckillServiceImpl.class);

	/**
     * 思考：为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
     */
	private Lock lock = new ReentrantLock(true);//互斥锁 参数默认false，不公平锁  

    @Autowired
    SeckillMapper seckillMapper;
	@Autowired
	SeckillResultMapper seckillResultMapper;

	@Override
	public PageInfo<Map<String,Object>> getSeckillList() {
        PageHelper.startPage(1, 20);

		List<Map<String,Object>> list = seckillMapper.selectAll();
		return new PageInfo<>(list);
	}


	@Override
	@Transactional
	public Result startSeckill(long seckillId,long userId) {
		//校验库存
		Long number =  seckillMapper.selectItemNum(seckillId);
		if(number>0){
			//扣库存
			seckillMapper.updateItemNum(seckillId);
			//创建订单

			Map<String,Object> item = new HashMap<>();
			item.put("seckillId",seckillId);
			item.put("userId",userId);
			item.put("state",0);
			seckillResultMapper.Insert(item);

			//支付

			return Result.success();
		}else{
			return Result.error();
		}
	}


	@Override
	@Servicelock(lockid = "seckillId")
	@Transactional
	public Result startSeckilRedisLock(long seckillId,long userId) {
		try {
			Long number =  seckillMapper.selectItemNum(seckillId);
			if(number>0){
				//扣库存
				seckillMapper.updateItemNum(seckillId);
				//创建订单

				Map<String,Object> item = new HashMap<>();
				item.put("seckillId",seckillId);
				item.put("userId",userId);
				item.put("state",0);
				seckillResultMapper.Insert(item);
			}else{
				return Result.error();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success();
	}


}
