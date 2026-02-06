package com.clock_in.clock.service;

import com.clock_in.clock.model.ClockEntry;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.ClockEntryRepository;
import com.clock_in.core.exceptions.AlreadyClockedInException;
import com.clock_in.core.exceptions.AlreadyClockedOutException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        return clockEntryRepository.save(entry);
    }
}
