package com.sogon.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor; // 기본 생성자 추가 (JSON 파싱용)

import java.time.LocalDate;

@Data
public class DiaryWriteDto {

    @NotBlank(message = "일기를 작성해주세요.")
    private String content;

    @NotNull(message = "날짜를 입력해주세요.")
    private LocalDate date; // YYYY-MM-DD 형식으로 받음

    private String sentiment; // "기쁨", "슬픔" 등 (선택 사항)
}