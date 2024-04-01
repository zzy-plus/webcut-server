package com.zzy.webcut.common;

/**
 * 基于ThreadLocal封装的工具类，用于保存和获取当前登录的用户id
 */
public class BaseContext {
    private static  ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setIp(String ip){
        threadLocal.set(ip);
    }

    public static String getIp(){
        return threadLocal.get();
    }
}
