package com.sogon.server.controller;

import com.sogon.server.dto.DiaryWriteDto;
import com.sogon.server.service.DiaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    // 일기 쓰기 (POST /diary)
    @PostMapping
    public String writeDiary(@Valid @RequestBody DiaryWriteDto dto, Principal principal) {
        // Principal: 토큰 검사를 통과한 유저의 ID(이메일)가 들어있음
        String email = principal.getName();

        diaryService.writeDiary(email, dto);
        return "일기 저장 성공!";
    }
}