package com.library.smart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.smart.entity.BorrowRecord;
import com.library.smart.mapper.BorrowRecordMapper;
import com.library.smart.service.BorrowRecordService;
import org.springframework.stereotype.Service;

@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {
}
