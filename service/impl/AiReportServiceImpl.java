package com.library.smart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.smart.entity.AiReport;
import com.library.smart.mapper.AiReportMapper;
import com.library.smart.service.AiReportService;
import org.springframework.stereotype.Service;

@Service
public class AiReportServiceImpl extends ServiceImpl<AiReportMapper, AiReport> implements AiReportService {
}
