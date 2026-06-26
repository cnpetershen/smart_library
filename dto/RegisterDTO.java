package com.library.smart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank(message = "账号不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "账号长度为3-20位，只能包含字母、数字和下划线")
    private String account;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "密码长度为6-20位")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "确认密码长度为6-20位")
    private String confirmPassword;

    @NotBlank(message = "姓名不能为空")
    @Pattern(regexp = "^.{2,50}$", message = "姓名长度为2-50位")
    private String name;

    private String gender;

    private String phone;

    private String email;
}
