package com.github.glo2003.payroll.exceptions;

import com.github.glo2003.payroll.employees.Employee;

public class NotEnoughHolidaysRemainingException extends Exception {
    public NotEnoughHolidaysRemainingException(Employee e) {
        super("Employee " + e.getName() + " has not enough holidays remaining");
    }
}
