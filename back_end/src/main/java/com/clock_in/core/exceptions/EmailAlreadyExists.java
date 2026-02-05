package com.clock_in.core.exceptions;

public class EmailAlreadyExists extends AppGenericException {
    public EmailAlreadyExists()
    {
        super("Email already in use", "EMAIL_ALREADY_EXISTS");
    }
}
