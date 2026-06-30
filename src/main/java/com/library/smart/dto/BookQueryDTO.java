package com.library.smart.dto;

import lombok.Data;

@Data
public class BookQueryDTO {

    private Integer page = 1;

    private Integer pageSize = 10;

    private String keyword;

    private String category;

    private Integer status;

    private String sortField = "createTime";

    private String sortOrder = "desc";
}
