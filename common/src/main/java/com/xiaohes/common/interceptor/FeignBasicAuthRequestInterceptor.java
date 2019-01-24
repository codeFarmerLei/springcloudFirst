package com.xiaohes.common.interceptor;

import com.alibaba.fescar.core.context.RootContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

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




            //添加请求头
            HttpServletRequest request = getHttpServletRequest();
            if (request != null){
                setHeaders(request,requestTemplate);
                //setBody(request,requestTemplate);
            }


        } catch (Throwable e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }


    /**
     * 获取request
     * @return
     */
    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加请求头
     * @param request
     * @param requestTemplate
     */
    private void setHeaders(HttpServletRequest request,RequestTemplate requestTemplate) {
        //Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration == null) {
            return;
        }
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            requestTemplate.header(key, value);

            //map.put(key, value);
        }
        //return map;
    }

    /**
     * 添加请求参数
     * @param request
     * @param requestTemplate
     */
    private void setBody(HttpServletRequest request,RequestTemplate requestTemplate) {
        Enumeration<String> bodyNames = request.getParameterNames();
        if (bodyNames == null) {
            return;
        }
        StringBuffer body = new StringBuffer();
        while (bodyNames.hasMoreElements()) {
            String name = bodyNames.nextElement();
            String values = request.getParameter(name);
            body.append(name).append("=").append(values).append("&");
        }
        if(body.length()>0) {
            body.deleteCharAt(body.length()-1);
            requestTemplate.body(body.toString());
            //log.info("feign interceptor body:{}",body.toString());
        }
    }
}
