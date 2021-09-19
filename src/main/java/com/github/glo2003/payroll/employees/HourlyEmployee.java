package com.github.glo2003.payroll.employees;

import com.github.glo2003.payroll.Role;
import com.github.glo2003.payroll.exceptions.NotEnoughHolidaysRemainingException;

public class HourlyEmployee extends Employee {
    private final float workedHoursFor2Weeks;
    private float hourlyRate;

    public HourlyEmployee(String name, Role role, int vacationDays, float hourlyRate, float workedHoursFor2Weeks) {
        super(name, role, vacationDays);
        this.hourlyRate = hourlyRate;
        this.workedHoursFor2Weeks = workedHoursFor2Weeks;
    }

    @Override
    public float getPayForTwoWeeks() {
        return workedHoursFor2Weeks * hourlyRate;
    }

    @Override
    public void giveRaise(float raise) {
        hourlyRate += raise;
    }

    @Override
    public float takePayout() throws NotEnoughHolidaysRemainingException {
        takeHoliday(NUM_DAYS_PAYOUT);
        return workedHoursFor2Weeks * hourlyRate / 2f;
    }

    @Override
    public String toString() {
        return "HourlyEmployee{" +
                "name='" + this.getName() + '\'' +
                ", role='" + this.getRole() + '\'' +
                ", vacation_days=" + this.getVacationDays() +
                ", hourlyRate=" + hourlyRate +
                ", workedHoursFor2Weeks=" + workedHoursFor2Weeks +
                '}';
    }
}
