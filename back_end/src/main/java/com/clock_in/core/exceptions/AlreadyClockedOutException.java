package com.clock_in.core.exceptions;

public class AlreadyClockedOutException extends AppGenericException {
    public AlreadyClockedOutException() {
        super("Employee is already clocked out", "ALREADY_CLOCKED_OUT");
    }
}
