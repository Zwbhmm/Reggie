package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 实现公共字段自动填充功能
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 公共字段自动填充之插入数据时调用
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insertFill...");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //从线程中获取到登录用户ID
        metaObject.setValue("createUser", BaseContext.getId());
        metaObject.setValue("updateUser",BaseContext.getId());
    }

    /**
     * 公共字段自动填充之更新数据时调用
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("updateFill...");
        metaObject.setValue("updateUser", BaseContext.getId());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
}
