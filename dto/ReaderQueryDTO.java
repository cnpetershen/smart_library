package com.library.smart.dto;

import lombok.Data;

@Data
public class ReaderQueryDTO {

    private Integer page = 1;

    private Integer pageSize = 10;

    private String keyword;

    private Integer status;

    private String gender;

    private String sortField = "registerTime";

    private String sortOrder = "desc";
}
