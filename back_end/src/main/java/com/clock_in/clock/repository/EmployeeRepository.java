package com.clock_in.clock.repository;

import com.clock_in.clock.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>,
    JpaSpecificationExecutor<Employee> {

        // Find employee by email
        Optional<Employee> findByEmail(String email);

        // Find employee by full name
        Optional<Employee> findByFullName(String fullName);

        // Find employee by leave status
        Optional<Employee> findByIsOnLeave(boolean isOnLeave);

        // Find employee by office
        Optional<Employee> findByOffice(String office);

        // Find employee by isCurrentlyWorking status
        Optional<Employee> findByClockEntries_ClockOutTimeIsNull();
    }
