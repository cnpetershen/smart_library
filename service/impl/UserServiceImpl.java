package com.library.smart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.smart.common.BusinessException;
import com.library.smart.dto.RegisterDTO;
import com.library.smart.dto.UpdatePasswordDTO;
import com.library.smart.dto.UpdateProfileDTO;
import com.library.smart.entity.User;
import com.library.smart.mapper.UserMapper;
import com.library.smart.service.UserService;
import com.library.smart.utils.JwtUtil;
import com.library.smart.vo.TokenVO;
import com.library.smart.vo.UserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public TokenVO login(String account, String password, String role) {
        User user = this.lambdaQuery()
                .eq(User::getAccount, account)
                .one();

        if (user == null) {
            throw new BusinessException(400, "账号不存在");
        }

        if (!user.getRole().equals(role)) {
            throw new BusinessException(400, "角色不匹配");
        }

        if (user.getStatus() == 0) {
            throw new BusinessException(400, "账号已被禁用");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(400, "密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getAccount(), user.getRole());

        UserVO userVO = new UserVO(
                user.getId(),
                user.getAccount(),
                user.getName(),
                user.getRole(),
                user.getGender(),
                user.getPhone(),
                user.getEmail(),
                user.getAvatar(),
                user.getStatus(),
                user.getRegisterTime()
        );

        return new TokenVO(token, userVO);
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        if (existsByAccount(registerDTO.getAccount())) {
            throw new BusinessException(400, "账号已存在");
        }

        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException(400, "两次密码输入不一致");
        }

        User user = new User();
        user.setAccount(registerDTO.getAccount());
        user.setPassword(passwordEncoder.encode(registerDTO.getConfirmPassword()));
        user.setName(registerDTO.getName());
        user.setRole("reader");
        user.setGender(registerDTO.getGender());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setStatus(1);
        user.setRegisterTime(LocalDateTime.now());

        this.save(user);
    }

    @Override
    public User getCurrentUser(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return user;
    }

    @Override
    public void updateProfile(Long userId, UpdateProfileDTO updateProfileDTO) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        if (StringUtils.hasText(updateProfileDTO.getName())) {
            user.setName(updateProfileDTO.getName());
        }
        if (updateProfileDTO.getGender() != null) {
            user.setGender(updateProfileDTO.getGender());
        }
        if (StringUtils.hasText(updateProfileDTO.getPhone())) {
            user.setPhone(updateProfileDTO.getPhone());
        }
        if (StringUtils.hasText(updateProfileDTO.getEmail())) {
            user.setEmail(updateProfileDTO.getEmail());
        }
        if (StringUtils.hasText(updateProfileDTO.getAvatar())) {
            user.setAvatar(updateProfileDTO.getAvatar());
        }

        this.updateById(user);
    }

    @Override
    public void updatePassword(Long userId, UpdatePasswordDTO updatePasswordDTO) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        if (!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException(400, "旧密码错误");
        }

        if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmPassword())) {
            throw new BusinessException(400, "两次密码输入不一致");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        this.updateById(user);
    }

    @Override
    public boolean existsByAccount(String account) {
        return this.lambdaQuery().eq(User::getAccount, account).count() > 0;
    }
}
