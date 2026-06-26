package com.library.smart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private Long id;
    private String account;
    private String name;
    private String role;
    private String gender;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    private LocalDateTime registerTime;
}
