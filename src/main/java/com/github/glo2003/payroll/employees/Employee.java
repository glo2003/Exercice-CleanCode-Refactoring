package com.github.glo2003.payroll.employees;


import com.github.glo2003.payroll.Role;
import com.github.glo2003.payroll.exceptions.NotEnoughHolidaysRemainingException;

public abstract class Employee {
    protected static final int NUM_DAYS_PAYOUT = 5;

    private final String name;
    private final Role role;
    protected int vacationDays;
    private boolean hasTakenHoliday;

    public Employee(String name, Role role, int vacationDays) {
        this.name = name;
        this.role = role;
        this.vacationDays = vacationDays;
        this.hasTakenHoliday = false;
    }

    abstract public float getPayForTwoWeeks();

    abstract public void giveRaise(float raise);

    abstract public float takePayout() throws NotEnoughHolidaysRemainingException;

    public void takeHoliday(int amount) throws NotEnoughHolidaysRemainingException {
        if (vacationDays < amount) {
            throw new NotEnoughHolidaysRemainingException(this);
        }
        vacationDays -= amount;
        hasTakenHoliday = true;
    }

    public boolean hasTakenHolidays() {
        return hasTakenHoliday;
    }

    public void resetHasTakenHolidays() {
        hasTakenHoliday = false;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public int getVacationDays() {
        return vacationDays;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", vacationDays=" + vacationDays +
                '}';
    }
}
