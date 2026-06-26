package com.library.smart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.smart.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
