package com.clock_in.core.exceptions;

public class EmployeeNotFoundException extends AppGenericException {

    public EmployeeNotFoundException() {
        super("Employee not found", "EMPLOYEE_NOT_FOUND");
    }
}