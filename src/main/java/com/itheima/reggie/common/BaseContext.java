package com.itheima.reggie.common;

/***
 * 封装ThreadLocal工具类，设置添加用户Id和读取用户Id的功能
 */
public class BaseContext{
    private static ThreadLocal<Long> threadLocal =new ThreadLocal<>();

    /**
     * 设置Id
     * @param id
     */
    public static void setId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取Id
     * @return
     */
    public static  Long getId(){
       return threadLocal.get();
    }

}
