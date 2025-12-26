package com.sogon.server.repository;

import com.sogon.server.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// JpaRepository를 상속하여 Diary 엔티티에 대한 CRUD 및 쿼리
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    // 추가적인 쿼리 메서드가 필요하면 여기에 정의
    // 예: 특정 사용자의 모든 일기 조회 등
    
}
