package com.clock_in.clock.repository;

import com.clock_in.clock.model.ClockEntry;
import com.clock_in.clock.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClockEntryRepository
        extends JpaRepository<ClockEntry, Long> {

    // All entries for an employee (history, reports, etc.)
    List<ClockEntry> findByEmployee(Employee employee);

    // All currently active clock entries (admin use, monitoring)
    List<ClockEntry> findByClockOutTimeIsNull();

    // Active entry for an employee (should be at most one)
    Optional<ClockEntry> findFirstByEmployeeAndClockOutTimeIsNull(Employee employee);

    // All entries within a date range (reporting, analytics)
    List<ClockEntry> findByClockInTimeBetween(LocalDateTime start, LocalDateTime end);

    // find by uuid
    Optional<ClockEntry> findByUuid(String uuid);

    // Fast existence check (used for validation)
    boolean existsByEmployeeAndClockOutTimeIsNull(Employee employee);
}
