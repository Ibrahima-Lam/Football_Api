package com.fscore.app.exception;

public class AuthException extends RuntimeException {
    public AuthException() {
        super("Invalid email or password");
    }
}
