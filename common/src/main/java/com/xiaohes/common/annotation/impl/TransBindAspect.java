package com.xiaohes.common.annotation.impl;

import com.alibaba.fescar.core.context.RootContext;
import com.alibaba.fescar.core.rpc.RpcContext;
import com.xiaohes.common.annotation.Servicelock;
import com.xiaohes.common.annotation.TransBind;
import com.xiaohes.common.redis.RedisUtil;
import com.xiaohes.common.redis.RedissLockUtil;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步锁 AOP
 */
@Component
@Scope
@Aspect
@Order(1)//order越小越是最先执行 order默认值是2147483647
public class TransBindAspect {

	private static final Logger log = LoggerFactory.getLogger(TransBindAspect.class);

	//切点
	@Pointcut("@annotation(com.xiaohes.common.annotation.TransBind)")
	public void bindAspect() {
		
	}

	@Autowired
	private RedisUtil redisUtil;

    @Around("bindAspect() && @annotation(anno)")
    public  Object around(ProceedingJoinPoint joinPoint, TransBind anno) {

		Object obj = null;
		boolean bind = false;
		String rpcXid = null;

		try {
			String xid = RootContext.getXID();
			Serializable cacheId = redisUtil.get(RootContext.KEY_XID);
			if (cacheId!= null){
				rpcXid = cacheId.toString();
			}

			log.info("=======================================bind=======================================");
			log.info("xid in RootContext[" + xid + "] xid in redis[" + rpcXid + "]");

			if (xid != null) {
				redisUtil.add(RootContext.KEY_XID, xid);
			} else {
				if (rpcXid != null) {
					RootContext.bind(rpcXid);
					bind = true;
					log.info("bind[" + rpcXid + "] to RootContext");
				}
			}




			obj = joinPoint.proceed();





		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		} finally {
			if (bind) {
				String unbindXid = RootContext.unbind();
				log.info("unbind[" + unbindXid + "] from RootContext");

				if (!rpcXid.equalsIgnoreCase(unbindXid)) {
					log.info("xid in change during RPC from " + rpcXid + " to " + unbindXid);
					if (unbindXid != null) {
						RootContext.bind(unbindXid);
						log.info("bind [" + unbindXid + "] back to RootContext");
					}
				}
				log.info("=======================================unbind=======================================");
			}
		}
    	return obj;
    } 
}
