package com.library.smart.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String account;

    private String password;

    private String name;

    private String role;

    private String gender;

    private String phone;

    private String email;

    private String avatar;

    private Integer status;

    private LocalDateTime registerTime;

    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
