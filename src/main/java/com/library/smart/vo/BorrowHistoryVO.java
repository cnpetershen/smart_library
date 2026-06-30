package com.library.smart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowHistoryVO {

    private Long id;

    private Long bookId;

    private String bookTitle;

    private LocalDateTime borrowTime;

    private LocalDateTime expectedReturnTime;

    private LocalDateTime actualReturnTime;

    private Integer overdueDays;

    private Integer status;

    private String statusText;

    public static String getStatusText(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "借阅中";
            case 1 -> "已归还";
            case 2 -> "已逾期";
            default -> "未知";
        };
    }
}
