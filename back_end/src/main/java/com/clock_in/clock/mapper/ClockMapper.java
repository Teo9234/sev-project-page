package com.clock_in.clock.mapper;

import com.clock_in.clock.dto.*;
import com.clock_in.clock.model.ClockEntry;
import com.clock_in.clock.model.Employee;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ClockMapper {

    // -----------------------------
    // Employee mappings
    // -----------------------------

    public static EmployeeResponseDTO toEmployeeDTO(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setUuid(employee.getUuid().toString());
        dto.setFullName(employee.getFullName());
        dto.setEmail(employee.getEmail());
        dto.setRole(employee.getRole());
        dto.setOffice(employee.getOffice());
        dto.setOnLeave(employee.isOnLeave());
        dto.setCurrentlyWorking(employee.isCurrentlyWorking());
        return dto;
    }


    // -----------------------------
    // ClockEntry mappings
    // -----------------------------

    public static ClockEntryResponseDTO toClockEntryDTO(ClockEntry entry) {
        ClockEntryResponseDTO dto = new ClockEntryResponseDTO();
        dto.setEmployeeUuid(entry.getEmployee().getUuid().toString());

        // Convert LocalDateTime -> Long timestamp
        dto.setClockInTime(toEpochMilli(entry.getClockInTime()));
        dto.setClockOutTime(entry.getClockOutTime() != null ? toEpochMilli(entry.getClockOutTime()) : null);

        // Set lastModified as epoch milli or null if not set
        dto.setLastModified(entry.getLastModified() != null ? toEpochMilli(entry.getLastModified()) : null);
        dto.setCurrentlyWorking(entry.getClockOutTime() == null);

        return dto;
    }

    public static ClockEntry toClockEntryEntity(ClockEntryRequestDTO dto, Employee employee) {
        ClockEntry entry = new ClockEntry();
        entry.setEmployee(employee);

        // Convert Long timestamp -> LocalDateTime
        entry.setClockInTime(toLocalDateTime(dto.getClockInTime()));
        if (dto.getClockOutTime() != null) {
            entry.setClockOutTime(toLocalDateTime(dto.getClockOutTime()));
        }

        return entry;
    }

    // -----------------------------
    // Helper methods for conversion
    // -----------------------------
    private static LocalDateTime toLocalDateTime(Long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    private static Long toEpochMilli(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
