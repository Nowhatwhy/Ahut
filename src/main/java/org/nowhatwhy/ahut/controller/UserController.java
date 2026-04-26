package org.nowhatwhy.ahut.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.dto.UserDTO;
import org.nowhatwhy.ahut.enitity.Result;
import org.nowhatwhy.ahut.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService IUserService;
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return Result.ok(IUserService.login(userDTO));
    }
    @PostMapping("/sendCode")
    public Result<?> sendCode(String qq) {
        IUserService.sendCode(qq);
        return Result.ok();
    }
}
