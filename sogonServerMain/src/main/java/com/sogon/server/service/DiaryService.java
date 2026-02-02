package com.sogon.server.service;

import com.sogon.server.dto.DiaryResponseDto; // 일기 응답 DTO
import com.sogon.server.dto.DiaryWriteDto; // 일기 작성 DTO
import com.sogon.server.entity.Diary; // 일기 엔티티
import com.sogon.server.entity.User; // 유저 엔티티
import com.sogon.server.repository.DiaryRepository; // 일기 리포지토리
import com.sogon.server.repository.UserRepository; // 유저 리포지토리
import org.springframework.beans.factory.annotation.Autowired; // 의존성 주입
import org.springframework.stereotype.Service; // 서비스 컴포넌트
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 관리

import java.util.List; // 리스트
import java.util.stream.Collectors; // 스트림 수집

@Service // 서비스 컴포넌트 선언
public class DiaryService { // 일기 서비스

    @Autowired // 의존성 주입
    private DiaryRepository diaryRepository;

    @Autowired // 의존성 주입
    private UserRepository userRepository;

    // 일기 쓰기
    @Transactional
    public void writeDiary(String email, DiaryWriteDto dto) { // 이메일로 유저 찾기
        User user = userRepository.findByEmail(email) // 이메일로 유저 찾기
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Diary diary = new Diary(); // 일기 엔티티 생성
        diary.setUser(user); // 작성자 설정
        diary.setContent(dto.getContent()); // 내용 설정
        diary.setWrittenDate(dto.getDate()); // 날짜 설정
        diary.setSentiment(dto.getSentiment()); // 감정 설정

        diaryRepository.save(diary); // 일기 저장
    }

    // 내 일기 목록 조회
    @Transactional(readOnly = true)
    public List<DiaryResponseDto> getMyDiaries(String email) { // 이메일로 유저 찾기
        User user = userRepository.findByEmail(email) // 이메일로 유저 찾기
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 1. DB에서 내 일기 다 가져오기
        List<Diary> diaries = diaryRepository.findAllByUserOrderByWrittenDateDesc(user);

        // 2. Entity -> DTO 변환
        return diaries.stream().map(diary -> {
            DiaryResponseDto dto = new DiaryResponseDto(); // 새로운 DTO 상자 만들기
            dto.setId(diary.getDiaryId()); // 일기 번호도 담아주기
            dto.setContent(diary.getContent()); // 내용
            dto.setDate(diary.getWrittenDate()); // 날짜
            dto.setSentiment(diary.getSentiment()); // 감정
            return dto; // 상자 반환
        }).collect(Collectors.toList()); // 상자들을 리스트로 묶어서 반환
    }
    // 일기 수정
    @Transactional
    public void updateDiary(Long diaryId, String email, DiaryWriteDto dto){
        // 일기 찾기
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));
        
        // 본인 확인
        if (!diary.getUser().getEmail().equals(email)) {
            throw new RuntimeException("본인의 일기만 수정할 수 있습니다.");
        }

        // 내용 갈아끼우기 (Diary Checking: 저장(save) 안 해도 DB가 바뀜)
        diary.setContent(dto.getContent());
        diary.setWrittenDate(dto.getDate());
        diary.setSentiment(dto.getSentiment());
    }
    // 일기 삭제
    @Transactional
    public void deleteDiary(Long diaryId, String email){
        // 일기 찾기
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("일기가 존재하지 않습니다."));
        
        // 작성자 확인
        if (!diary.getUser().getEmail().equals(email)){
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }

        diaryRepository.delete(diary);  // DB에서 삭제
    }
}