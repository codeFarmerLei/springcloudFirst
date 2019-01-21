package com.xiaohes.common.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by lei
 * @date 2019-1-18 9:42
 */

public class Result extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public Result() {
        put("status", 200);
    }

    public static Result error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static Result error(String msg) {
        return error(500, msg);
    }

    public static Result error(int status, String msg) {
        Result r = new Result();
        r.put("status", status);
        r.put("msg", msg);
        return r;
    }
    public static Result error(Object data) {
        Result r = new Result();
        r.put("data", data);
        r.put("status",500);
        return r;
    }
    public static Result success(Object data) {
        Result r = new Result();
        r.put("data", data);
        r.put("status", 200);
        r.put("msg", "success");
        return r;
    }


    public static Result success(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result success() {
        return new Result();
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
