package com.zzy.webcut.common;

import lombok.Data;

@Data
public class R<T> {
    private Integer code;
    private T data;
    private String msg;

    public static <T> R<T> success(T data){
        R<T> r = new R<>();
        r.code = 0;
        r.data = data;
        return r;
    }

    public static <T> R<T> error(String msg){
        R r = new R();
        r.code = 1;
        r.msg = msg;
        return r;
    }

    public static <T> R<T> error(int code ,String msg){
        R r = new R();
        r.code = code;
        r.msg = msg;
        return r;
    }
}
