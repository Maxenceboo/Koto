package com.koto.service;

import com.koto.domain.AppUser;
import com.koto.repository.AppUserRepository;
import com.koto.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AppUser register(String email, String usernameGlobal, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        AppUser user = AppUser.builder()
                .email(email)
                .usernameGlobal(usernameGlobal)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role("USER")
                .build();
        return userRepository.save(user);
    }

    public String issueToken(AppUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId().toString());
        claims.put("uname", user.getUsernameGlobal());
        claims.put("role", user.getRole());
        return jwtService.generateToken(user.getEmail(), claims);
    }
}

