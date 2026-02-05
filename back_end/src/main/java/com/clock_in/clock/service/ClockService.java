package com.clock_in.clock.service;

import com.clock_in.clock.model.ClockEntry;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.ClockEntryRepository;
import com.clock_in.core.exceptions.AlreadyClockedInException;
import com.clock_in.core.exceptions.AlreadyClockedOutException;
import com.clock_in.core.exceptions.AppGenericException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClockService {

    private final ClockEntryRepository clockEntryRepository;

    public ClockService(ClockEntryRepository clockEntryRepository) {
        this.clockEntryRepository = clockEntryRepository;
    }

    /**
     * Clock-in an employee.
     * Rules:
     * - Employee cannot clock in twice without clocking out
     */
    @Transactional
    public ClockEntry clockIn(Employee employee) throws AppGenericException {

        // Ask the database if there is already an active entry
        if (clockEntryRepository.existsByEmployeeAndClockOutTimeIsNull(employee)) {
            throw new AlreadyClockedInException();
        }

        // Create a new clock entry
        ClockEntry entry = new ClockEntry();
        entry.setEmployee(employee);
        entry.setClockInTime(LocalDateTime.now());

        // Save and return the persisted entity
        return clockEntryRepository.save(entry);
    }

    /**
     * Clock-out an employee.
     * Rules:
     * - Employee must currently be clocked in
     */
    @Transactional
    public ClockEntry clockOut(Employee employee) throws AppGenericException {

        // Find the active clock entry or fail
        ClockEntry entry = clockEntryRepository
                .findFirstByEmployeeAndClockOutTimeIsNull(employee)
                .orElseThrow(AlreadyClockedOutException::new);

        entry.setClockOutTime(LocalDateTime.now());

        return clockEntryRepository.save(entry);
    }
}
