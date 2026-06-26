package com.library.smart.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_book")
public class Book {

    @TableId(type = IdType.AUTO)
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

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
