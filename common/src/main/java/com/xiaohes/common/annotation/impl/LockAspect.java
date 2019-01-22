package com.xiaohes.common.annotation.impl;

import com.xiaohes.common.annotation.Servicelock;
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
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
public class LockAspect {

	private static final Logger log = LoggerFactory.getLogger(LockAspect.class);

	/**
     * 思考：为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
     */
	private static  Lock lock = new ReentrantLock(true);//互斥锁 参数默认false，不公平锁  
	
	//切点
	@Pointcut("@annotation(com.xiaohes.common.annotation.Servicelock)")
	public void lockAspect() {
		
	}
	
    @Around("lockAspect() && @annotation(anno)")
    public  Object around(ProceedingJoinPoint joinPoint, Servicelock anno) {

		Object obj = null;
		boolean res = false;
		String seckillId = anno.lockpre();

		try {
			Signature signature = joinPoint.getSignature();
			// 请求的方法名
			String strMethodName = signature.getName();
			// 请求的类名
			String strClassName = joinPoint.getTarget().getClass().getName();

			// 请求的参数
			Object[] params = joinPoint.getArgs();
			MethodSignature methodSignature = (MethodSignature) signature;
			//2.最关键的一步:通过这获取到方法的所有参数名称的字符串数组
			String[] parameterNames = methodSignature.getParameterNames();
			if (StringUtils.isNotEmpty(anno.lockid()) && params != null && params.length > 0 && parameterNames != null) {
				for (int i = 0; i < parameterNames.length; i++) {
					String parameterName = parameterNames[i];
					if (parameterName.equals(anno.lockid())){
						seckillId = seckillId + params[i];
						break;
					}
				}
			}

			/**
			 * 尝试获取锁，最多等待2秒，上锁以后10秒自动解锁
			 */
			log.info("==============分布式锁id："+seckillId);
			res = RedissLockUtil.tryLock(seckillId, TimeUnit.SECONDS, 2, 10);
			if(res) {
				obj = joinPoint.proceed();
			}
			else {
				log.info("分布式加锁失败{}.{}.{}",strClassName,strMethodName,seckillId);
			}




		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		} finally{
			if(res){//释放锁
				RedissLockUtil.unlock(seckillId);
			}
		}
    	return obj;
    } 
}
