package com.library.smart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReaderVO {

    private Long id;

    private String account;

    private String name;

    private String gender;

    private String phone;

    private String email;

    private String avatar;

    private Integer status;

    private String statusText;

    private LocalDateTime registerTime;

    private Integer borrowCount;

    private Integer overdueCount;

    public static String getStatusText(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "禁用";
            case 1 -> "正常";
            default -> "未知";
        };
    }
}
