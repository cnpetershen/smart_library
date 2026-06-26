package com.library.smart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.smart.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
