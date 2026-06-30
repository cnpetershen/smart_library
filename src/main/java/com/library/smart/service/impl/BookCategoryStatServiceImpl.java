package com.library.smart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.smart.entity.BookCategoryStat;
import com.library.smart.mapper.BookCategoryStatMapper;
import com.library.smart.service.BookCategoryStatService;
import org.springframework.stereotype.Service;

@Service
public class BookCategoryStatServiceImpl extends ServiceImpl<BookCategoryStatMapper, BookCategoryStat> implements BookCategoryStatService {
}
