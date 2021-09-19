package com.github.glo2003.payroll.employees;

import com.github.glo2003.payroll.Role;
import com.github.glo2003.payroll.exceptions.NotEnoughHolidaysRemainingException;

public class SalariedEmployee extends Employee {
    private float biweeklySalary;

    public SalariedEmployee(String name, Role role, int vacationDays, float biweeklySalary) {
        super(name, role, vacationDays);
        this.biweeklySalary = biweeklySalary;
    }

    @Override
    public float getPayForTwoWeeks() {
        return biweeklySalary;
    }

    @Override
    public void giveRaise(float raise) {
        biweeklySalary += raise;
    }

    @Override
    public float takePayout() throws NotEnoughHolidaysRemainingException {
        takeHoliday(NUM_DAYS_PAYOUT);
        return biweeklySalary / 2f;
    }

    @Override
    public String toString() {
        return "SalariedEmployee{" +
                "name='" + this.getName() + '\'' +
                ", role='" + this.getRole() + '\'' +
                ", vacation_days=" + this.getVacationDays() +
                ", biweeklySalary=" + biweeklySalary +
                '}';
    }
}
