package com.library.smart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.smart.entity.UserTag;
import com.library.smart.mapper.UserTagMapper;
import com.library.smart.service.UserTagService;
import org.springframework.stereotype.Service;

@Service
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag> implements UserTagService {
}
