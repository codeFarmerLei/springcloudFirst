package com.xiaohes.common.annotation;
import java.lang.annotation.*;

/**
 * 自定义注解 同步锁
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface Servicelock {
	String lockpre() default "com.xiaohes.distributed:";
	String lockid() default "";
}
