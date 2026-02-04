package com.clock_in.core.exceptions;

public class ClockEntryNotFoundException extends AppGenericException {

    public ClockEntryNotFoundException() {
        super("Clock entry not found", "CLOCK_ENTRY_NOT_FOUND");
    }
}