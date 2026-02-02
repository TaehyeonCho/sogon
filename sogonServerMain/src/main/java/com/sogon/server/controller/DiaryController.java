package com.sogon.server.controller;

import com.sogon.server.dto.DiaryResponseDto;
import com.sogon.server.dto.DiaryWriteDto;
import com.sogon.server.service.DiaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    // 1. 쓰기
    @PostMapping
    public String writeDiary(@Valid @RequestBody DiaryWriteDto dto, Principal principal) {
        diaryService.writeDiary(principal.getName(), dto);
        return "🎉 일기 저장 성공!";
    }

    // 2. 조회
    @GetMapping
    public List<DiaryResponseDto> getMyDiaries(Principal principal) {
        return diaryService.getMyDiaries(principal.getName());
    }

    // 3.("id") 명시하여 해결)
    @PutMapping("/{id}")
    public String updateDiary(@PathVariable("id") Long id, @Valid @RequestBody DiaryWriteDto dto, Principal principal) {
        diaryService.updateDiary(id, principal.getName(), dto);
        return "🛠️ 일기 수정 성공!";
    }

    // 4. ("id") 명시하여 해결)
    @DeleteMapping("/{id}")
    public String deleteDiary(@PathVariable("id") Long id, Principal principal) {
        diaryService.deleteDiary(id, principal.getName());
        return "🗑️ 일기 삭제 성공!";
    }
}