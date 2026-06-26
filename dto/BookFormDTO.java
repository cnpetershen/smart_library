package com.library.smart.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookFormDTO {

    private Long id;

    @NotBlank(message = "书名不能为空")
    @Size(min = 1, max = 200, message = "书名长度为1-200位")
    private String title;

    @NotBlank(message = "作者不能为空")
    @Size(min = 1, max = 100, message = "作者姓名为1-100位")
    private String author;

    @NotBlank(message = "ISBN不能为空")
    @Pattern(regexp = "^[\\d-]+$", message = "ISBN必须为10位或13位数字")
    private String isbn;

    @NotBlank(message = "分类不能为空")
    @Size(min = 1, max = 50, message = "分类长度为1-50位")
    private String category;

    private String publishDate;

    @NotNull(message = "总库存不能为空")
    @Min(value = 1, message = "总库存必须大于0")
    private Integer totalStock;

    private String description;

    private String coverUrl;
}
