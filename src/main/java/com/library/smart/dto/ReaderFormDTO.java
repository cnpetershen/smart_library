package com.library.smart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReaderFormDTO {

    private Long id;

    @NotBlank(message = "账号不能为空")
    @Size(min = 3, max = 20, message = "账号长度为3-20位")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "账号只能包含字母、数字和下划线")
    private String account;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 1, max = 50, message = "姓名为1-50位")
    private String name;

    private String gender;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private Integer status;
}
