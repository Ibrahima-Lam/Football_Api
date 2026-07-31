package com.fscore.app.service;

import com.fscore.app.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(String email, String password);
}
