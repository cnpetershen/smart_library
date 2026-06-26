package com.library.smart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BorrowDTO {

    @NotNull(message = "图书ID不能为空")
    private Long bookId;

    @NotNull(message = "读者ID不能为空")
    private Long userId;
}
