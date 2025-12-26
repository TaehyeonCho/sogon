package com.sogon.server.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class UserSignUpDto {

    @NotBlank(message = "아이디(이메일)는 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$",
             message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~15자여야 합니다.")
    private String password;

    private String passwordCheck;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "생년월일을 입력해주세요.")
    // 정규식 설명: 숫자4개 - 숫자2개 - 숫자2개 (예: 1997-09-14)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일은 YYYY-MM-DD 형식이어야 합니다.")
    private String birthDate;

    @NotBlank
    private String phone1;
    @NotBlank
    private String phone2;
    @NotBlank
    private String phone3;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 8, message = "닉네임은 2~8자 사이여야 합니다.")
    private String nickname;

    private String mbti;

    private List<String> hobbies;
}