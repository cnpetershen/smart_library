package com.library.smart.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_ai_report")
public class AiReport {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String reportType;

    private String title;

    @TableField("report_title")
    private String reportTitle;

    @TableField("report_content")
    private String reportContent;

    private LocalDateTime generateTime;

    private Integer isDeleted;
}
