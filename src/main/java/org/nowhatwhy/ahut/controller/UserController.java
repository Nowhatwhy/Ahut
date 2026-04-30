package org.nowhatwhy.ahut.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.dto.UserDTO;
import org.nowhatwhy.ahut.dto.UserUpdateDTO;
import org.nowhatwhy.ahut.entity.Result;
import org.nowhatwhy.ahut.service.IUserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户
 * @author nowhatwhy
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return Result.ok(userService.login(userDTO));
    }
    @PostMapping("/sendCode")
    public Result<?> sendCode(String qq) {
        userService.sendCode(qq);
        return Result.ok();
    }
    @PutMapping("/update")
    public Result<?> update(@RequestBody UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userUpdateDTO);
        return Result.ok();
    }
}
