package com.zzy.webcut.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Records {
    private Long id;
    private Long clipboardId;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime time;
    private Integer action;
    private String main;
}
