package com.xiaohes.oauth;

import com.xiaohes.common.annotation.Servicelock;
import com.xiaohes.common.interceptor.BodyReaderHttpServletRequestWrapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
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

    //@Autowired
    //private OAuth2ClientProperties oAuth2ClientProperties;
    //@Autowired
    //private BaseOAuth2ProtectedResourceDetails baseOAuth2ProtectedResourceDetails;
    private WebResponseExceptionTranslator exceptionTranslator = new DefaultWebResponseExceptionTranslator();
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenExtractor tokenExtractor;

    @Override
    @Servicelock
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        try {
            //解析异常，如果是401则处理
            ResponseEntity<?> result = exceptionTranslator.translate(authException);
            if (result.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                String refresh_token = System.getProperty("xiaohes.auth.refresh_token");
                if (StringUtils.isEmpty(refresh_token)){
                    //如果是网页,跳转到登陆页面
                    response.sendRedirect("/user/login");
                    return;
                }
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
                formData.add("client_id", "web");//oAuth2ClientProperties.getClientId(),oAuth2ClientProperties.getClientSecret()
                formData.add("client_secret", "123456");
                formData.add("grant_type", "refresh_token");

                formData.add("refresh_token",refresh_token);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.add("Authorization","Basic dXNlci1zZXJ2aWNlOjEyMzQ1Ng==");


                Map map = restTemplate.exchange("http://localhost:7979/oauth/token", HttpMethod.POST,
                        new HttpEntity<MultiValueMap<String, String>>(formData,headers), Map.class).getBody();

                //如果刷新异常,则坐进一步处理
                if(map == null || map.get("error") != null){
                    //如果是网页,跳转到登陆页面
                    response.sendRedirect("/user/login");
                    log.info("=============================刷新token失败，请重新登录=============================");
                }else{
                    log.info("=============================刷新token成功=============================");
                    //如果刷新成功则存储cookie并且跳转到原来需要访问的页面
                    //Cookie[] cookie = request.getCookies();
                    String access_token = String.valueOf(map.get("access_token"));
                    refresh_token = String.valueOf(map.get("refresh_token"));
                    System.setProperty("xiaohes.auth.refresh_token", refresh_token);
                    System.setProperty("xiaohes.auth.access_token", access_token);


                    BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
                    requestWrapper.addHeader("Authorization","bearer "+access_token);

                    log.info(requestWrapper.getHeader("Authorization"));


                    Authentication authentication = tokenExtractor.extract(requestWrapper);
                    Authentication authResult = authenticationManager.authenticate(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                    requestWrapper.getRequestDispatcher(request.getRequestURI()).forward(requestWrapper,response);

                    //String str = "abc";
                    //response.setHeader("Content-Type","text/html");
                    //response.getWriter().write(str);
                    //response.flushBuffer();
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

}
