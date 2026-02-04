package com.clock_in.clock.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClockEntryResponseDTO {

    private String employeeUuid;
    private Long clockInTime;
    private Long clockOutTime; // null if not clocked out
    private LocalDateTime lastModified;

    // optional computed field
    private boolean currentlyWorking;
}
