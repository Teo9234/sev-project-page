package com.clock_in.core.exceptions;

public class EmployeeOnLeaveException extends AppGenericException {
    public EmployeeOnLeaveException() {
        super("Employee is currently on leave", "EMPLOYEE_ON_LEAVE");
    }
}
