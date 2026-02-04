package com.clock_in.core.exceptions;

public class NotClockedInException extends AppGenericException {
    public NotClockedInException() {
        super("Employee is not currently clocked in", "NOT_CLOCKED_IN");
    }
}
