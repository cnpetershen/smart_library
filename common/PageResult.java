package com.library.smart.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long total;
    private List<T> records;
    private Long page;
    private Long pageSize;
    private Long totalPages;

    public static <T> PageResult<T> of(Long total, List<T> records, Long page, Long pageSize) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setRecords(records);
        result.setPage(page);
        result.setPageSize(pageSize);
        result.setTotalPages((total + pageSize - 1) / pageSize);
        return result;
    }
}
