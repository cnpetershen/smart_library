package com.library.smart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecordVO {

    private Long id;

    private Long bookId;

    private String bookTitle;

    private Long userId;

    private String readerName;

    private LocalDateTime borrowTime;

    private LocalDateTime expectedReturnTime;

    private LocalDateTime actualReturnTime;

    private Integer overdueDays;

    private Integer status;

    private String statusText;

    private String overdueStatus;

    public static String getStatusText(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "借阅中";
            case 1 -> "已归还";
            default -> "未知";
        };
    }

    public String getOverdueStatus() {
        if (status == null) return "未知";
        if (status == 1) {
            if (overdueDays != null && overdueDays > 0) {
                return "已逾期" + overdueDays + "天";
            }
            return "正常归还";
        }
        if (LocalDateTime.now().isAfter(
                expectedReturnTime != null ? expectedReturnTime : LocalDateTime.now())) {
            return "已逾期";
        }
        return "正常";
    }
}
