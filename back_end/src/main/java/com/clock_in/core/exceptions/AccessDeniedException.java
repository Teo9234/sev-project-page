package com.clock_in.core.exceptions;

public class AccessDeniedException extends AppGenericException {
    public AccessDeniedException() {
        super("Access denied", "ACCESS_DENIED");
    }
}
