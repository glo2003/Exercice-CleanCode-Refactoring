package com.github.glo2003.payroll;

public class SalariedEmployee extends Employee {
    private float biweeklySalary;

    public SalariedEmployee(String name, String role, int vacation_days, float biweeklySalary) {
        super(name, role, vacation_days);
        this.biweeklySalary = biweeklySalary;
    }

    public float getBiweeklySalary() {
        return biweeklySalary;
    }

    public void setBiweeklySalary(float biweeklySalary) {
        this.biweeklySalary = biweeklySalary;
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
