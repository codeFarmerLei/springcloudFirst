package com.xiaohes.common.annotation.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.sun.media.jfxmedia.logging.Logger;
import com.xiaohes.common.bean.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 限流 AOP
 */
@Component
@Scope
@Aspect
public class LimitAspect {
	////每秒只发出5个令牌，此处是单进程服务的限流,内部采用令牌捅算法实现
	private static RateLimiter rateLimiter = RateLimiter.create(5.0);
	
	//Service层切点  限流
	@Pointcut("@annotation(com.xiaohes.common.annotation.ServiceLimit)")
	public void ServiceAspect() {
		
	}

	@Autowired
	private HttpServletResponse response;

    @Around("ServiceAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
    	Boolean flag = rateLimiter.tryAcquire();
    	Object obj = null;
		try {
			if(flag){
				obj = joinPoint.proceed();
			}else {

				if (response != null){
					String result = Result.error(100, "failure").toString();
					output(response, result);
				}

			}
		} catch (Throwable e) {
			e.printStackTrace();
		} 
    	return obj;
    }

	public void output(HttpServletResponse response, String msg) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			outputStream.write(msg.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			outputStream.flush();
			outputStream.close();
		}
	}

}
