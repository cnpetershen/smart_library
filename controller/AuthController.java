package com.library.smart.controller;

import com.library.smart.common.Result;
import com.library.smart.dto.LoginDTO;
import com.library.smart.dto.RegisterDTO;
import com.library.smart.dto.UpdatePasswordDTO;
import com.library.smart.dto.UpdateProfileDTO;
import com.library.smart.entity.User;
import com.library.smart.security.LoginUser;
import com.library.smart.service.UserService;
import com.library.smart.vo.TokenVO;
import com.library.smart.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<TokenVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        TokenVO tokenVO = userService.login(
                loginDTO.getAccount(),
                loginDTO.getPassword(),
                loginDTO.getRole()
        );
        return Result.success(tokenVO);
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success(null);
    }

    @GetMapping("/profile")
    public Result<UserVO> getProfile() {
        LoginUser loginUser = getCurrentUser();
        User user = userService.getCurrentUser(loginUser.getUserId());

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

        return Result.success(userVO);
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@Valid @RequestBody UpdateProfileDTO updateProfileDTO) {
        LoginUser loginUser = getCurrentUser();
        userService.updateProfile(loginUser.getUserId(), updateProfileDTO);
        return Result.success(null);
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        LoginUser loginUser = getCurrentUser();
        userService.updatePassword(loginUser.getUserId(), updatePasswordDTO);
        return Result.success(null);
    }

    private LoginUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (LoginUser) authentication.getPrincipal();
    }
}
