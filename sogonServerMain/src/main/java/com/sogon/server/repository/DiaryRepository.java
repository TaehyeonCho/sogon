package com.sogon.server.repository;

import com.sogon.server.entity.Diary;
import com.sogon.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    // 특정 유저의 일기를 날짜 최신순(Desc)으로 찾음
    List<Diary> findAllByUserOrderByWrittenDateDesc(User user);
}