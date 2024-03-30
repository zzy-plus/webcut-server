package com.zzy.webcut.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if(metaObject.hasSetter("createTime")){
            metaObject.setValue("createTime", localDateTime);
        }
        if(metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime", localDateTime);
        }
        if(metaObject.hasSetter("time")){
            metaObject.setValue("time", localDateTime);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime localDateTime = LocalDateTime.now();

        if(metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime", localDateTime);
        }
        if(metaObject.hasSetter("time")){
            metaObject.setValue("time", localDateTime);
        }
    }
}
