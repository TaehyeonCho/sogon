package com.sogon.server.service;

import com.sogon.server.dto.UserLoginDto; 
import com.sogon.server.dto.UserSignUpDto;
import com.sogon.server.entity.User;
import com.sogon.server.repository.UserRepository;
import com.sogon.server.util.JwtUtil; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil; // 토큰 발급기

    // [회원가입]
    @Transactional
    public void signUp(UserSignUpDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setNickname(dto.getNickname());
        user.setMbti(dto.getMbti());
        
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        String fullPhone = dto.getPhone1() + "-" + dto.getPhone2() + "-" + dto.getPhone3();
        user.setPhone(fullPhone);

        LocalDate birth = LocalDate.parse(dto.getBirthDate(), DateTimeFormatter.ISO_DATE);
        user.setBirthDate(birth);

        if (dto.getHobbies() != null && !dto.getHobbies().isEmpty()) {
            String hobbyString = String.join(",", dto.getHobbies());
            user.setHobbies(hobbyString);
        }

        userRepository.save(user);
    }

    // 로그인
    public String login(UserLoginDto dto) {
        // 1. 이메일로 유저 찾기
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        // 2. 비밀번호 대조 (암호화된 비번이랑 비교해야 함)
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        // 3. 맞으면 토큰(출입증) 발급해서 리턴
        return jwtUtil.generateToken(user.getEmail());
    }
}