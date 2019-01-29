package com.xiaohes.common.interceptor;


import com.xiaohes.common.utils.HttpHelper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 用于将request重新进行封装
 * @author by lei
 * @date 2019-1-29 10:51
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    private Map<String,String> headerMap;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 提取原本的HEADER内容
        Enumeration enumeration = request.getHeaderNames();
        headerMap = new HashMap<String,String>();
        while(enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            headerMap.put(name, request.getHeader(name));
        }

        body = HttpHelper.getBodyString(request).getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }


    public void addHeader(String name,String value) {
        headerMap.put(name, value);
    }


    @Override
    public String getHeader(String name) {
        return headerMap.get(name);
    }

    @Override
    public Enumeration getHeaders(String name) {
        if ("authorization".equalsIgnoreCase(name)){
            Vector headers = new Vector();
            headers.add(name);
            headers.add(getHeader(name));
            return headers.elements();
        }else {
            return super.getHeaders(name);
        }

    }

    @Override
    public Enumeration getHeaderNames() {
        Set<String> set = new HashSet<String>(headerMap.keySet());
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }
        // create an enumeration from the set and return
        return Collections.enumeration(set);
    }

    public Map getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map headerMap) {
        this.headerMap = headerMap;
    }

}
