package com.refrigerator.backend.web.user;

import com.refrigerator.backend.config.security.JwtTokenProvider;
import com.refrigerator.backend.domain.user.User;
import com.refrigerator.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.Map;

// Controller : 회원 가입과 로그인 인증 과정
@RequiredArgsConstructor
@RestController
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입!!
    public Long join(@RequestBody Map<String, String> user) {
        return userRepository.save(User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getId();
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        User member = userRepository.findByEmail(user.get("emai"))
                .orElseThrow(() -> new IllegalStateException("가입되지 않은 E-mail입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
}

// 애플리케이션 실행 후 POSTMAN 활용해 테스트
// join : 회원가입
// login : 로그인 (Post 요청)

// 참고 : https://webfirewood.tistory.com/115
