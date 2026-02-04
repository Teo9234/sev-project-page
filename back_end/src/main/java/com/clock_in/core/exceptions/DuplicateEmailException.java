package com.clock_in.core.exceptions;

public class DuplicateEmailException extends AppGenericException {

    public DuplicateEmailException(String email) {
        super(
                "An employee with email '" + email + "' already exists",
                "DUPLICATE_EMAIL"
        );
    }
}
