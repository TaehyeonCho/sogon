package com.sogon.server.controller;

import com.sogon.server.dto.UserLoginDto;
import com.sogon.server.dto.UserSignUpDto;
import com.sogon.server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String signUp(@Valid @RequestBody UserSignUpDto userSignUpDto) {
        userService.signUp(userSignUpDto);
        return "회원가입을 완료하였습니다!!";
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto userLoginDto) {
        String token = userService.login(userLoginDto);
        return token;
    }
}