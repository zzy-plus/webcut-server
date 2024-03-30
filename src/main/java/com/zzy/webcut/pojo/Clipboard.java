package com.zzy.webcut.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Clipboard {
    private Long id;
    private String name;
    private String content;
    private Integer expiration;
    private String description;
    private Integer isPrivate;
    private String password;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
//    @TableLogic(value="0", delval="1")
//    private Integer isDeleted;
}
