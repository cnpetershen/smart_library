package com.library.smart.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_user_tag")
public class UserTag {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String tagName;

    private String tagType;

    private LocalDateTime createTime;

    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
