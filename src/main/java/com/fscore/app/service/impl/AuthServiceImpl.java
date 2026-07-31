package com.fscore.app.service.impl;

import com.fscore.app.dto.response.LoginResponse;
import com.fscore.app.dto.response.UserResponse;
import com.fscore.app.entity.ApiUser;
import com.fscore.app.exception.AuthException;
import com.fscore.app.repository.ApiUserRepository;
import com.fscore.app.security.JwtService;
import com.fscore.app.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final ApiUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(ApiUserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(String email, String password) {
        ApiUser user = repository.findByEmail(email.trim().toLowerCase())
            .orElseThrow(AuthException::new);
        if (user.getPasswordHash() == null || !Boolean.TRUE.equals(user.getActive())) {
            throw new AuthException();
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AuthException();
        }
        String token = jwtService.generateToken(user);
        UserResponse userResponse = UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .plan(user.getPlan())
            .active(user.getActive())
            .build();
        return LoginResponse.builder()
            .token(token)
            .user(userResponse)
            .build();
    }
}
