package com.clock_in.clock.service;

import com.clock_in.clock.dto.ClockStatusResponseDTO;
import com.clock_in.clock.model.ClockEntry;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.ClockEntryRepository;
import com.clock_in.core.exceptions.AlreadyClockedInException;
import com.clock_in.core.exceptions.AlreadyClockedOutException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class ClockService {

    private final ClockEntryRepository clockEntryRepository;

    public ClockService(ClockEntryRepository clockEntryRepository) {
        this.clockEntryRepository = clockEntryRepository;
    }

    @Transactional
    public ClockEntry clockIn(Employee employee) throws AlreadyClockedInException {

        if (clockEntryRepository.existsByEmployeeAndClockOutTimeIsNull(employee)) {
            throw new AlreadyClockedInException();
        }

        ClockEntry entry = new ClockEntry();
        entry.setEmployee(employee);
        entry.setClockInTime(LocalDateTime.now());

        return clockEntryRepository.save(entry);
    }

    @Transactional
    public ClockEntry clockOut(Employee employee) throws AlreadyClockedOutException {

        ClockEntry entry = clockEntryRepository
                .findFirstByEmployeeAndClockOutTimeIsNull(employee)
                .orElseThrow(AlreadyClockedOutException::new);

        entry.setClockOutTime(LocalDateTime.now());
        entry.setLastModified(LocalDateTime.now());
        return clockEntryRepository.save(entry);
    }

    /**
     * Get the current clock status for an employee:
     * - Whether they are currently clocked in
     * - Their clock-in time (if active)
     * - Their last clock-out time (from the most recent completed entry)
     */
    public ClockStatusResponseDTO getStatus(Employee employee) {
        ClockStatusResponseDTO dto = new ClockStatusResponseDTO();
        dto.setEmployeeUuid(employee.getUuid().toString());

        // Check for an active (open) clock entry
        Optional<ClockEntry> activeEntry = clockEntryRepository
                .findFirstByEmployeeAndClockOutTimeIsNull(employee);

        if (activeEntry.isPresent()) {
            dto.setCurrentlyClockedIn(true);
            dto.setClockInTime(
                    activeEntry.get().getClockInTime().toEpochSecond(ZoneOffset.UTC)
            );
        } else {
            dto.setCurrentlyClockedIn(false);
            dto.setClockInTime(null);
        }

        // Find the most recent completed entry for "last clock out" time
        Optional<ClockEntry> lastCompleted = clockEntryRepository
                .findFirstByEmployeeAndClockOutTimeIsNotNullOrderByClockOutTimeDesc(employee);

        if (lastCompleted.isPresent()) {
            dto.setLastClockOutTime(
                    lastCompleted.get().getClockOutTime().toEpochSecond(ZoneOffset.UTC)
            );
        } else {
            dto.setLastClockOutTime(null);
        }

        return dto;
    }
}
