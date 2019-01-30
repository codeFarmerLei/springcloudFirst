package com.xiaohes.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

/**
 * BPwdEncoderUtil工具类
 * @author by lei
 * @date 2019-1-24 14:58
 */
public class BPwdEncoderUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String  BCryptPassword(String password){
        return encoder.encode(password);
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword){
        return encoder.matches(rawPassword,encodedPassword);
    }

    /**
     * 加密
     * @param str
     * @return
     */
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }
}
