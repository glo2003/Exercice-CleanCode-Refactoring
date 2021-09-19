package com.github.glo2003;

import com.github.glo2003.payroll.CompanyPayroll;
import com.github.glo2003.payroll.Employee;
import com.github.glo2003.payroll.HourlyEmployee;
import com.github.glo2003.payroll.SalariedEmployee;

public class Main {

    public static void main(String[] args) throws Exception {
        CompanyPayroll companyPayroll = new CompanyPayroll();

        Employee e1 = new HourlyEmployee("Alice", "vp", 25, 100, 35.5f * 4);
        Employee e2 = new SalariedEmployee("Bob", "engineer", 4, 1500);
        Employee e3 = new SalariedEmployee("Charlie", "manager", 4, 2000);
        Employee e4 = new HourlyEmployee("Ernest", "intern", 1, 5, 50 * 4);
        Employee e5 = new HourlyEmployee("Fred", "intern", 1, 5, 50 * 4);

        companyPayroll.addEmployee(e1);
        companyPayroll.addEmployee(e2);
        companyPayroll.addEmployee(e3);
        companyPayroll.addEmployee(e4);
        companyPayroll.addEmployee(e5);

        System.out.println("----- Listing employees -----");
        companyPayroll.listEmployees();

        System.out.println("----- Giving raises -----");
        companyPayroll.giveRaise(e1, 10);
        companyPayroll.giveRaise(e2, 100);

        System.out.println("\n----- Holidays -----");
        companyPayroll.takeHoliday(e1, true, null);
        companyPayroll.takeHoliday(e2, false, 10);
        companyPayroll.takeHoliday(e3, true, null);
        System.out.println("Number of employees in holidays: " + companyPayroll.getNumberOfEmployeesInHolidays());

        System.out.println("\n----- Create paychecks -----");
        companyPayroll.createPendingPaychecks();

        System.out.println("\n----- Pay statistics -----");
        float t = companyPayroll.getTotalMoney();
        System.out.println("Total money spent: ");
        float avg = companyPayroll.getAveragePendingPaycheck();
        System.out.println("Average paycheck: " + avg);

        System.out.println("\n----- Pay -----");
        companyPayroll.processPendingPaychecks();
    }
}
