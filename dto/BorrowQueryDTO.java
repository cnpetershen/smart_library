package com.library.smart.dto;

import lombok.Data;

@Data
public class BorrowQueryDTO {

    private Integer page = 1;

    private Integer pageSize = 10;

    private String keyword;

    private Integer status;

    private Integer overdueFilter;

    private String sortField = "borrowTime";

    private String sortOrder = "desc";
}
