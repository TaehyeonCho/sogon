package com.sogon.server.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DiaryResponseDto {
    private Long id;        // 일기 번호 (수정/삭제할 때 필요)
    private String content; // 내용
    private LocalDate date; // 날짜
    private String sentiment; // 감정
}