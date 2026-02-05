package com.clock_in.clock.dto;

import com.clock_in.clock.model.ClockEntry;
import java.time.ZoneOffset;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClockEntryResponseDTO {

    private String employeeUuid;
    private Long clockInTime;
    private Long clockOutTime; // null if not clocked out
    private Long lastModified;
    private boolean currentlyWorking;

    public static ClockEntryResponseDTO fromEntity(ClockEntry entry) {
        // Convert ClockEntry entity to DTO
        ClockEntryResponseDTO dto = new ClockEntryResponseDTO();

        // Map fields from entity to DTO
        dto.setEmployeeUuid(entry.getEmployee().getUuid().toString());

        // Convert LocalDateTime to Unix timestamp (seconds since epoch)
        dto.setClockInTime(
                entry.getClockInTime()
                        .toEpochSecond(ZoneOffset.UTC)
        );

        // clockOutTime can be null if the employee is currently working
        if (entry.getClockOutTime() != null) {
            dto.setClockOutTime(
                    entry.getClockOutTime()
                            .toEpochSecond(ZoneOffset.UTC)
            );
        }

        // lastModified can also be null if the entry was never modified after creation
        if (entry.getLastModified() != null) {
            dto.setLastModified(
                    entry.getLastModified()
                            .toEpochSecond(ZoneOffset.UTC)
            );
        }

        dto.setCurrentlyWorking(entry.isActive());

        return dto;
    }
}
