package com.clock_in.core.exceptions;

    public class LoginFailed extends AppGenericException {
    public LoginFailed() {
        super("Login failed: Invalid email or password", "LOGIN_FAILED");
    }
}
