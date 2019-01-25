package com.xiaohes.common.interceptor;

import com.xiaohes.common.annotation.Servicelock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * OAuth2.0 重写异常处理器
 * @author by lei
 * @date 2019-1-25 14:49
 */
@Component
public class MyAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(MyAuthenticationEntryPoint.class);

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;
    //@Autowired
    //private BaseOAuth2ProtectedResourceDetails baseOAuth2ProtectedResourceDetails;
    private WebResponseExceptionTranslator exceptionTranslator = new DefaultWebResponseExceptionTranslator();
    @Autowired
    RestTemplate restTemplate;

    @Override
    @Servicelock
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        try {
            //解析异常，如果是401则处理
            ResponseEntity<?> result = exceptionTranslator.translate(authException);
            if (result.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
                formData.add("client_id", oAuth2ClientProperties.getClientId());
                formData.add("client_secret", oAuth2ClientProperties.getClientSecret());
                formData.add("grant_type", "refresh_token");
                //Cookie[] cookie = request.getCookies();
                //for(Cookie coo:cookie){
                //    if(coo.getName().equals("refresh_token")){
                //        formData.add("refresh_token", coo.getValue());
                //        break;
                //    }
                //}
                formData.add("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJsZWkiLCJzY29wZSI6WyJzZXJ2aWNlIl0sImF0aSI6ImQyYmE5NGVkLTNkYmMtNDg2YS1hODQ0LWVkNjg4ZGViNmU3NSIsImV4cCI6MTU1MDk4ODc1MiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJkYjczNGU1ZS1jM2I2LTQzYjktOTI5NC05ZDc4Y2VlMzhhMzgiLCJjbGllbnRfaWQiOiJ1c2VyLXNlcnZpY2UifQ.goGRYgnSvI3R41aEk_kanRqjbWM3ohB95nXBMaQQVgtt3v7eHmjo7xT-ZDObZulJk39VBSwfHq2h9FEzifqJpVHLhEWEOH3zOaQeReujFGcgs7yUSMbweqK4uWdjNhnZtwnbfmoyU9WqS5Iu-EZbTV96dQKJXoNlnpisd1eUnkA6fDBQnARb4zcpnleI1MMpbccbVPD7nZqtwf-8cI3HkGmu_fT4TRDtykeWSrBPzO5QbhHCB3mSKZPtG69R85RxHhQCmhEE10qJhRxag-uaD4LUP40YHNRjBpuO4tKi6s3tq6_2T2IMg7BIudbPVSenjBx8R50a5tt_zuBp20Q0Qg");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                //baseOAuth2ProtectedResourceDetails.getAccessTokenUri()
                Map map = restTemplate.exchange("http://localhost:7979/oauth/token", HttpMethod.POST,
                        new HttpEntity<MultiValueMap<String, String>>(formData, headers), Map.class).getBody();
                //如果刷新异常,则坐进一步处理
                if(map == null || map.get("error") != null){
                    //// 返回指定格式的错误信息
                    //response.setStatus(401);
                    //response.setHeader("Content-Type", "application/json;charset=utf-8");
                    //response.getWriter().print("{\"code\":1,\"message\":\""+map.get("error_description")+"\"}");
                    //response.getWriter().flush();
                    //如果是网页,跳转到登陆页面
                    response.sendRedirect("/user/login");
                    log.info("=============================刷新token失败，请重新登录=============================");
                }else{
                    log.info("=============================刷新token成功=============================");
                    //如果刷新成功则存储cookie并且跳转到原来需要访问的页面
                    for(Object key:map.keySet()){
                        response.addCookie(new Cookie(key.toString(),map.get(key).toString()));
                        log.info("key:{},value:{}",key.toString(),map.get(key).toString());
                    }
                    request.getRequestDispatcher(request.getRequestURI()).forward(request,response);
                }
            }else{
                //如果不是401异常，则以默认的方法继续处理其他异常
                super.commence(request,response,authException);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
