package com.sogon.server.service;

import com.sogon.server.dto.DiaryWriteDto;
import com.sogon.server.entity.Diary;
import com.sogon.server.entity.User;
import com.sogon.server.repository.DiaryRepository;
import com.sogon.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final WebClient webClient;

    // 1. 일기 작성
    public Diary writeDiary(String email, DiaryWriteDto dto) {
        User user = getUser(email);

        // Python AI에게 벡터값 요청
        List<Double> vectorData = getEmbeddingFromAi(dto.getContent());

        Diary diary = Diary.builder()
                .user(user)
                .content(dto.getContent())
                .diaryDate(dto.getDate())       // [반영] DTO의 날짜
                .sentiment(dto.getSentiment())  // [반영] DTO의 감정
                .embedding(vectorData)
                .build();

        return diaryRepository.save(diary);
    }

    // 2. 내 일기 목록 조회
    @Transactional(readOnly = true)
    public List<Diary> getMyDiaries(String email) {
        User user = getUser(email);
        return diaryRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    // 3. 일기 수정
    public Diary updateDiary(Long id, String email, DiaryWriteDto dto) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일기를 찾을 수 없습니다."));
        
        if (!diary.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 내용 수정 시 벡터 재요청
        List<Double> newVectorData = getEmbeddingFromAi(dto.getContent());
        
        // [반영] 내용, 날짜, 감정, 벡터 모두 업데이트
        diary.update(dto.getContent(), dto.getDate(), dto.getSentiment(), newVectorData);
        
        return diary;
    }

    // 4. 일기 삭제
    public void deleteDiary(Long id, String email) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일기를 찾을 수 없습니다."));

        if (!diary.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        diaryRepository.delete(diary);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private List<Double> getEmbeddingFromAi(String text) {
        try {
            log.info("AI 임베딩 요청: {}", text.length() > 10 ? text.substring(0, 10) + "..." : text);
            Map response = webClient.post()
                    .uri("/embed")
                    .bodyValue(Map.of("text", text))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            if (response != null && response.containsKey("embedding")) {
                return (List<Double>) response.get("embedding");
            }
        } catch (Exception e) {
            log.error("❌ AI 서버 통신 실패", e);
        }
        return null;
    }
}