package com.library.smart.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_borrow_record")
public class BorrowRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bookId;

    private Long userId;

    private LocalDateTime borrowTime;

    private LocalDateTime expectedReturnTime;

    private LocalDateTime actualReturnTime;

    private Integer overdueDays;

    private Integer status;

    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
