package com.sogon.server.controller;

import com.sogon.server.dto.DiaryWriteDto;
import com.sogon.server.entity.Diary;
import com.sogon.server.service.DiaryService;
import jakarta.validation.Valid; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    // 1. 일기 작성
    @PostMapping
    public ResponseEntity<Diary> writeDiary(Principal principal, @RequestBody @Valid DiaryWriteDto dto) {
        // @Valid를 붙여야 DTO의 @NotNull, @NotBlank가 작동합니다.
        return ResponseEntity.ok(diaryService.writeDiary(principal.getName(), dto));
    }

    // 2. 내 일기 목록 조회
    @GetMapping
    public ResponseEntity<List<Diary>> getMyDiaries(Principal principal) {
        return ResponseEntity.ok(diaryService.getMyDiaries(principal.getName()));
    }

    // 3. 일기 수정
    @PutMapping("/{id}")
    public ResponseEntity<Diary> updateDiary(@PathVariable Long id, Principal principal, @RequestBody @Valid DiaryWriteDto dto) {
        return ResponseEntity.ok(diaryService.updateDiary(id, principal.getName(), dto));
    }

    // 4. 일기 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long id, Principal principal) {
        diaryService.deleteDiary(id, principal.getName());
        return ResponseEntity.ok().build();
    }
}