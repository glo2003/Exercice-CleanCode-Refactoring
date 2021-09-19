package com.github.glo2003.payroll;


public abstract class Employee {
    private final String name;
    private final String role;
    private int vacationDays;

    public Employee(String name, String role, int vacationDays) {
        this.name = name;
        this.role = role;
        this.vacationDays = vacationDays;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public int getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(int vacationDays) {
        this.vacationDays = vacationDays;
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
