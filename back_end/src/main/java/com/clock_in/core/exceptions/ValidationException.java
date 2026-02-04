package com.clock_in.core.exceptions;

public class ValidationException extends AppGenericException {
    public ValidationException(String message, String code) {
        super(message, code);
    }
}
