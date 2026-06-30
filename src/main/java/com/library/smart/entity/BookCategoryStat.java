package com.library.smart.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("t_book_category_stat")
public class BookCategoryStat {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String category;

    private Integer borrowCount;

    private LocalDate statDate;

    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
