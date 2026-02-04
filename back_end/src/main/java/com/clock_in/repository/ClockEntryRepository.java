package com.clock_in.repository;

import com.clock_in.model.ClockEntry;
import com.clock_in.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClockEntryRepository extends
        JpaRepository<ClockEntry, Long>,
        JpaSpecificationExecutor<ClockEntry> {

    // Find all clock entries for a specific employee
    List<ClockEntry> findByEmployee(Employee employee);

    // Find all active clock entries (not yet clocked out)
    List<ClockEntry> findByClockOutTimeIsNull();

    // Optional: find all entries for a specific employee that are active
    List<ClockEntry> findByEmployeeAndClockOutTimeIsNull(Employee employee);
}
