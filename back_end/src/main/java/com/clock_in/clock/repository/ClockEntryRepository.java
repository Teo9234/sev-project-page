package com.clock_in.clock.repository;

import com.clock_in.clock.model.ClockEntry;
import com.clock_in.clock.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClockEntryRepository extends JpaRepository<ClockEntry, Long> {

    // Find all clock entries for a specific employee (used for history, reporting)
    List<ClockEntry> findByEmployee(Employee employee);

    // Find all active clock entries (clocked in but not out) (used for dashboard, admin view)
    List<ClockEntry> findByClockOutTimeIsNull();

    // Find the most recent active clock entry for a specific employee (used for dashboard, validation)
    Optional<ClockEntry> findFirstByEmployeeAndClockOutTimeIsNull(Employee employee);

    // Find all clock entries within a specific time range (used for reporting, admin functions)
    List<ClockEntry> findByClockInTimeBetween(LocalDateTime start, LocalDateTime end);

    // Find by UUID (used for API endpoints, internal logic)
    Optional<ClockEntry> findByUuid(UUID uuid);

    // Fast existence check for active clock entry by employee (used for validation before clock in)
    boolean existsByEmployeeAndClockOutTimeIsNull(Employee employee);
}
