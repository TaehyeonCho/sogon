package com.sogon.server.repository;

import com.sogon.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository 
// JpaRepository를 상속하여 User 엔티티에 대한 CRUD 및 쿼리 메서드 제공 - CRUD 기능
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 조회 메서드
    Optional<User> findByEmail(String email);
}
