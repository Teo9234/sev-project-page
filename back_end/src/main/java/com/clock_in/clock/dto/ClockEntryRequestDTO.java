package com.clock_in.clock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClockEntryRequestDTO {

    @NotBlank(message = "Employee UUID is required")
    private String employeeUuid;

    @NotNull(message = "Clock-in time is required")
    @Positive(message = "Clock-in time must be a positive Unix timestamp")
    private Long clockInTime;

    @Positive(message = "Clock-out time must be a positive Unix timestamp")
    private Long clockOutTime; // optional for clock-out
}
