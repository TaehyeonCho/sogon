package com.sogon.server.controller;

import com.sogon.server.entity.User;
import com.sogon.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

    
@RestController // 1. "나 안내 데스크(Controller)야"
public class HelloController {

    @Autowired // 2. "스프링아, 아까 만든 '회원 관리자(Repository)' 좀 데려와줘." (의존성 주입)
    private UserRepository userRepository;

    @GetMapping("/test-db") // 3. "localhost:8080/test-db 주소로 오면 이 함수 실행해!"
    public String testDb() {
        // 4. 가짜 유저 데이터 만들기 (종이에 적는 과정)
        User newUser = new User();
        newUser.setEmail("test@sogon.com");
        newUser.setNickname("테스트유저");
        newUser.setPassword("1234"); // (실제론 암호화해야 하지만, 지금은 연결 확인용이라 그냥 넣습니다)

        // 5. 관리자에게 "저장해!"라고 시키기
        try {
            userRepository.save(newUser); // 이 한 줄이 SQL의 "INSERT INTO users..."를 대신해줍니다.
            return "🎉 DB 저장 성공! DBeaver에서 users 테이블을 확인해보세요.";
        } catch (Exception e) {
            return "😢 실패 (이미 있거나 에러 발생): " + e.getMessage();
        }
    }
}