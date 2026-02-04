package com.clock_in.core.exceptions;

public class AlreadyClockedInException extends AppGenericException {
    public AlreadyClockedInException() {
        super("Employee is already clocked in", "ALREADY_CLOCKED_IN");
    }
}
