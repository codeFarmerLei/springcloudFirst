package com.xiaohes.common.interceptor;

import com.xiaohes.common.annotation.Servicelock;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.http.*;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
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
                formData.add("client_id", "user-service");
                formData.add("client_secret", "123456");
                formData.add("grant_type", "refresh_token");

                String refresh_token = System.getProperty("xiaohes.auth.refresh_token");//Cookie[] cookie = request.getCookies();
                //formData.add("refresh_token",refresh_token);
                formData.add("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJsZWkiLCJzY29wZSI6WyJzZXJ2aWNlIl0sImF0aSI6IjY1ZGUxM2JhLTBjYzAtNGIyZC1hN2QyLWEwMDNjNjhmM2M1OCIsImV4cCI6MTU1MTAwMDAwMywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJlYzM3NzA0Ny1iNjdiLTQxMWQtOTliNC0wYjk1OTE3YTA3M2IiLCJjbGllbnRfaWQiOiJ1c2VyLXNlcnZpY2UifQ.fPjSH5ihkYLMKxFg9iV_pnhQa6sBXUqGS457L9Ku3OqFvjlBcVWjTC1PuMfKlrQbew_xOt6ZwKIrOrl4gXUVWQHP_iGMQMyVeV3v_sP2R3YF-D5ejPQX79V1HB9xtcFCFI-Ui4ZvENnqmTkUQZMQmHRXssCz2IqhczaSZVN6kGNzA0cES-IynxoA12AjQq5Qf_E2mty3dJnyfHWY_7ZlRKg6vGueJWUBMnY1M7sQ95MHFfBqFEQooSws7SgG4z6d9_GPHv3k6CiT5GV50JdtnRqRcS8pxsA0IcPej0Vo2LHVyx8oZ9pSHevS1YUd9VRVm5vle7nkGw2gcq5hU1Hopw");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.add("Authorization","Basic dXNlci1zZXJ2aWNlOjEyMzQ1Ng==");

                //http://localhost:7979/oauth/token
                RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
                Map map = restTemplate.exchange("http://localhost:7979/oauth/token", HttpMethod.POST,
                        new HttpEntity<MultiValueMap<String, String>>(formData,headers), Map.class).getBody();

                //如果刷新异常,则坐进一步处理
                if(map == null || map.get("error") != null){
                    //// 返回指定格式的错误信息
                    //response.setStatus(401);
                    //response.setHeader("Content-Type", "application/json;charset=utf-8");
                    //response.getWriter().print("{\"code\":1,\"message\":\""+map.get("error_description")+"\"}");
                    //response.getWriter().flush();
                    //如果是网页,跳转到登陆页面
                    response.sendRedirect("/user/login");
                    log.info("=============================刷新token失败，请重新登录{},{}=============================",
                            oAuth2ClientProperties.getClientId(),oAuth2ClientProperties.getClientSecret());
                }else{
                    log.info("=============================刷新token成功{},{},{}=============================",
                            oAuth2ClientProperties.getClientId(),oAuth2ClientProperties.getClientSecret(),request.getRequestURI());
                    //如果刷新成功则存储cookie并且跳转到原来需要访问的页面
                    System.setProperty("xiaohes.auth.refresh_token", String.valueOf(map.get("refresh_token")));
                    System.setProperty("xiaohes.auth.access_token", String.valueOf(map.get("access_token")));
                    request.getRequestDispatcher(request.getRequestURI()).forward(request,response);
                    //response.sendRedirect(request.getRequestURI());
                }
            }else{
                //如果不是401异常，则以默认的方法继续处理其他异常
                super.commence(request,response,authException);
            }
        } catch (Exception e) {
            log.info("刷新tooken异常");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        String refresh_token = System.getProperty("xiaohes.auth.access_token");//Cookie[] cookie = request.getCookies();
        System.out.println(refresh_token);



        //String access_token = System.getProperty("xiaohes.auth.access_token");
        //String request_token = String.valueOf(requestTemplate.headers().get("Authorization"));
        //if (StringUtils.isNotEmpty(access_token) && !access_token.equals(request_token)
        //        || StringUtils.isEmpty(request_token.replace("null","")) && request_token.startsWith("bearer ")){
        //    requestTemplate.header("Authorization",access_token);
        //}
        //log.info(access_token+",==========拦截器=========,"+request_token);
    }
}
