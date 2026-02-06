package com.clock_in.clock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClockStatusResponseDTO {

    private String employeeUuid;
    private boolean currentlyClockedIn;
    private Long clockInTime;   // epoch seconds, null if not currently clocked in
    private Long lastClockOutTime; // epoch seconds, null if never clocked out
}