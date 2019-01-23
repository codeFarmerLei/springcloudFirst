package com.xiaohes.common.interceptor;

import com.alibaba.fescar.core.context.RootContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author by lei
 * @date 2019-1-23 14:26
 */
@Configuration
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FeignBasicAuthRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {



            String xid = RootContext.getXID();
            if (xid != null) {
                requestTemplate.header(RootContext.KEY_XID, xid);
                log.info("========================header 添加 xid{}", xid);
            }

            /*
            //添加请求头
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    requestTemplate.header(name, values);

                }
                log.info("feign interceptor header:{}",requestTemplate);
            }

            //添加请求参数
            Enumeration<String> bodyNames = request.getParameterNames();
            StringBuffer body =new StringBuffer();
            if (bodyNames != null) {
                while (bodyNames.hasMoreElements()) {
                    String name = bodyNames.nextElement();
                    String values = request.getParameter(name);
                    body.append(name).append("=").append(values).append("&");
                }
            }
            if(body.length()!=0) {
                body.deleteCharAt(body.length()-1);
                requestTemplate.body(body.toString());
                //log.info("feign interceptor body:{}",body.toString());
            }*/


        } catch (Throwable e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }
}
