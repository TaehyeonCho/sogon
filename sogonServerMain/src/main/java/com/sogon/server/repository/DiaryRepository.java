package com.sogon.server.repository;

import com.sogon.server.entity.Diary;
import com.sogon.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findAllByUserOrderByCreatedAtDesc(User user);
}