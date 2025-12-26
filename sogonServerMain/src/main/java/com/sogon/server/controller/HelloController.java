package com.sogon.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

    
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "안녕? 나는 소곤이야. 서버와 DB가 연결되었어!";
    }
}
