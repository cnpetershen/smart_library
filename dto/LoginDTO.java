package com.library.smart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "账号不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "账号长度为3-20位，只能包含字母、数字和下划线")
    private String account;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "密码长度为6-20位")
    private String password;

    @NotBlank(message = "角色不能为空")
    @Pattern(regexp = "^(admin|reader)$", message = "角色只能是admin或reader")
    private String role;
}
