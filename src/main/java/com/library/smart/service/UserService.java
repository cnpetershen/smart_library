package com.library.smart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.library.smart.dto.RegisterDTO;
import com.library.smart.dto.UpdatePasswordDTO;
import com.library.smart.dto.UpdateProfileDTO;
import com.library.smart.entity.User;
import com.library.smart.vo.TokenVO;

public interface UserService extends IService<User> {

    TokenVO login(String account, String password, String role);

    void register(RegisterDTO registerDTO);

    User getCurrentUser(Long userId);

    void updateProfile(Long userId, UpdateProfileDTO updateProfileDTO);

    void updatePassword(Long userId, UpdatePasswordDTO updatePasswordDTO);

    boolean existsByAccount(String account);
}
