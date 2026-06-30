package com.library.smart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookVO {

    private Long id;

    private String title;

    private String author;

    private String isbn;

    private String category;

    private LocalDate publishDate;

    private String coverUrl;

    private Integer totalStock;

    private Integer availableStock;

    private Integer status;

    private String statusText;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static String getStatusText(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "可借";
            case 1 -> "已借出";
            case 2 -> "已下架";
            default -> "未知";
        };
    }
}
