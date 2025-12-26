package com.sogon.server.service;

import com.sogon.server.dto.DiaryWriteDto;
import com.sogon.server.entity.Diary;
import com.sogon.server.entity.User;
import com.sogon.server.repository.DiaryRepository;
import com.sogon.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiaryService {

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void writeDiary(String email, DiaryWriteDto dto) {
        // 1. 토큰에서 뽑은 이메일로 작성한 유저 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 2. 일기 Entity 생성
        Diary diary = new Diary();
        diary.setUser(user); 
        diary.setContent(dto.getContent());
        diary.setWrittenDate(dto.getDate());
        diary.setSentiment(dto.getSentiment());

        // 3. 저장
        diaryRepository.save(diary);
    }
}