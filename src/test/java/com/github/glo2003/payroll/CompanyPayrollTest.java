package com.github.glo2003.payroll;

import com.github.glo2003.payroll.employees.Employee;
import com.github.glo2003.payroll.employees.HourlyEmployee;
import com.github.glo2003.payroll.employees.SalariedEmployee;
import com.github.glo2003.payroll.exceptions.EmployeeDoesNotWorkHereException;
import com.github.glo2003.payroll.exceptions.InvalidRaiseException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

class CompanyPayrollTest {

    public static final float HOURLY_RATE = 10;
    public static final float HOURLY_AMOUNT = 25;
    public static final String HOURLY_NAME = "William";
    public static final String SALARIED_NAME = "Xavier";
    public static final float BIWEEKLY_AMOUNT = 10_000;
    public static final float RAISE = 10;
    public static final float ANOTHER_BIWEEKLY_AMOUNT = 20_000;
    public static final int VACATION_DAYS = 12;

    CompanyPayroll company;
    Employee vp;
    Employee eng;
    Employee manager;
    Employee intern1;
    Employee intern2;
    HourlyEmployee hourlyEmployee;
    SalariedEmployee salariedEmployee;
    SalariedEmployee anotherSalariedEmployee;

    @BeforeEach
    void setUp() {
        company = new CompanyPayroll();
        vp = new HourlyEmployee("Alice", Role.VICE_PRESIDENT, 25, 100, 35.5f * 2);
        eng = new SalariedEmployee("Bob", Role.ENGINEER, 4, 1500);
        manager = new SalariedEmployee("Charlie", Role.MANAGER, 10, 2000);
        intern1 = new HourlyEmployee("Ernest", Role.INTERN, 10, 5, 50 * 2);
        intern2 = new HourlyEmployee("Fred", Role.INTERN, 10, 5, 50 * 2);

        hourlyEmployee = new HourlyEmployee(HOURLY_NAME, Role.ENGINEER, VACATION_DAYS, HOURLY_RATE, HOURLY_AMOUNT);
        salariedEmployee = new SalariedEmployee(SALARIED_NAME, Role.ENGINEER, VACATION_DAYS, BIWEEKLY_AMOUNT);
        anotherSalariedEmployee = new SalariedEmployee("Yan", Role.MANAGER, VACATION_DAYS, ANOTHER_BIWEEKLY_AMOUNT);
    }

    @Test
    void createPendingPaychecksCreatesCorrectHourlyPaycheck() {
        company.addEmployee(hourlyEmployee);

        company.createPendingPaychecks();

        Paycheck paycheck = company.getPendings().get(0);
        assertThat(paycheck.getTo()).isEqualTo(HOURLY_NAME);
        assertThat(paycheck.getAmount()).isEqualTo(HOURLY_RATE * HOURLY_AMOUNT);
    }

    @Test
    void createPendingPaychecksCreatesCorrectSalariedPaycheck() {
        company.addEmployee(salariedEmployee);

        company.createPendingPaychecks();

        Paycheck paycheck = company.getPendings().get(0);
        assertThat(paycheck.getTo()).isEqualTo(SALARIED_NAME);
        assertThat(paycheck.getAmount()).isEqualTo(BIWEEKLY_AMOUNT);
    }

    @Test
    void processPendingPaychecks_shouldRemovePendingPaychecks() {
        company.addEmployee(vp);
        company.addEmployee(eng);
        company.addEmployee(manager);
        company.addEmployee(intern1);
        company.addEmployee(intern2);
        company.createPendingPaychecks();

        company.processPendingPaychecks();

        assertThat(company.getPendings().size()).isEqualTo(0);
    }

    @Test
    void createPendingPaychecks_shouldCreatePendingPaycheck() {
        company.addEmployee(vp);
        company.addEmployee(eng);
        company.addEmployee(manager);
        company.addEmployee(intern1);
        company.addEmployee(intern2);

        company.createPendingPaychecks();

        assertThat(company.getPendings().size()).isEqualTo(5);
    }

    @Test
    void findEngineers_shouldReturnEngineers() {
        company.addEmployee(eng);

        List<Employee> es = company.findEngineers();
        assertThat(es).containsExactly(eng);
    }

    @Test
    void findManagers_shouldReturnManagers() {
        company.addEmployee(manager);

        List<Employee> es = company.findManagers();
        assertThat(es).containsExactly(manager);
    }

    @Test
    void findVicePresidents_shouldReturnVicePresidents() {
        company.addEmployee(vp);

        List<Employee> es = company.findVicePresidents();
        assertThat(es).containsExactly(vp);
    }

    @Test
    void findInterns_shouldReturnInterns() {
        company.addEmployee(intern1);
        company.addEmployee(intern2);

        List<Employee> es = company.findInterns();
        assertThat(es).containsExactly(intern1, intern2);
    }

    @Test
    void createPendingPaychecksForHourlyEmployee_shouldReturnCorrectAmount() {
        company.addEmployee(vp);
        company.addEmployee(eng);
        company.addEmployee(manager);
        company.addEmployee(intern1);
        company.addEmployee(intern2);

        company.createPendingPaychecks();

        assertThat(company.getPendings().size()).isEqualTo(5);
    }

    @Test
    void giveRaiseToHourlyEmployee_shouldRaiseHourlySalary() throws Exception {
        company.addEmployee(hourlyEmployee);

        company.giveRaise(hourlyEmployee, RAISE);

        company.createPendingPaychecks();
        Paycheck paycheck = company.getPendings().get(0);
        assertThat(paycheck.getAmount()).isEqualTo((HOURLY_RATE + RAISE) * HOURLY_AMOUNT);
    }

    @Test
    void giveRaiseToSalariedEmployee_shouldRaiseMonthlySalary() throws Exception {
        company.addEmployee(salariedEmployee);

        company.giveRaise(salariedEmployee, RAISE);

        company.createPendingPaychecks();
        Paycheck paycheck = company.getPendings().get(0);
        assertThat(paycheck.getAmount()).isEqualTo(BIWEEKLY_AMOUNT + RAISE);
    }

    @Test
    void negativeRaise_shouldThrow() {
        company.addEmployee(eng);

        Assert.assertThrows(InvalidRaiseException.class, () -> company.giveRaise(eng, -1));
    }

    @Test
    void giveRaiseToAnAbsentEmployee_shouldThrow() {
        Assert.assertThrows(EmployeeDoesNotWorkHereException.class, () -> company.giveRaise(eng, 10));
    }

    @Test
    void salariedPayoutHoliday_shouldPayOneWeek() throws Exception {
        company.addEmployee(salariedEmployee);

        company.takePayout(salariedEmployee);

        Paycheck pending = company.getPendings().get(0);
        assertThat(pending.getAmount()).isEqualTo(BIWEEKLY_AMOUNT / 2);
        assertThat(salariedEmployee.getVacationDays()).isEqualTo(VACATION_DAYS - 5);
    }


    @Test
    void salariedHolidays_shouldRemovesVacantionDays() throws Exception {
        company.addEmployee(salariedEmployee);
        int amount = 2;

        company.takeHoliday(salariedEmployee, amount);

        assertThat(company.getPendings()).hasSize(0);
        assertThat(salariedEmployee.getVacationDays()).isEqualTo(VACATION_DAYS - amount);
    }

    @Test
    void hourlyPayoutHoliday_shouldPayOneWeek() throws Exception {
        company.addEmployee(hourlyEmployee);

        company.takePayout(hourlyEmployee);

        Paycheck pending = company.getPendings().get(0);
        assertThat(pending.getAmount()).isEqualTo(HOURLY_AMOUNT * HOURLY_RATE / 2f);
        assertThat(hourlyEmployee.getVacationDays()).isEqualTo(VACATION_DAYS - 5);
    }


    @Test
    void hourlyHolidays_shouldRemovesVacantionDays() throws Exception {
        company.addEmployee(hourlyEmployee);
        int amount = 2;

        company.takeHoliday(hourlyEmployee, amount);

        assertThat(company.getPendings()).hasSize(0);
        assertThat(hourlyEmployee.getVacationDays()).isEqualTo(VACATION_DAYS - amount);
    }

    @Test
    void getAveragePendingPaycheck_shouldReturnAverageAmount() throws Exception {
        company.addEmployee(salariedEmployee);
        company.addEmployee(anotherSalariedEmployee);
        company.createPendingPaychecks();

        float avg = company.getAveragePendingPaycheck();

        assertThat(avg).isEqualTo((BIWEEKLY_AMOUNT + ANOTHER_BIWEEKLY_AMOUNT) / 2);
    }

    @Test
    void getTotalMoney_shouldReturnTotal() {
        company.addEmployee(salariedEmployee);
        company.addEmployee(anotherSalariedEmployee);
        company.createPendingPaychecks();

        float t = company.getTotalMoney();

        assertThat(t).isEqualTo(BIWEEKLY_AMOUNT + ANOTHER_BIWEEKLY_AMOUNT);
    }

    @Test
    void getNumberOfEmployeesInHolidays_shouldReturnZeroWhenEveryoneWorks() {
        company.addEmployee(vp);
        company.addEmployee(eng);
        company.addEmployee(manager);
        company.addEmployee(intern1);
        company.addEmployee(intern2);

        long x = company.getNumberOfEmployeesInHolidays();

        assertThat(x).isEqualTo(0);
    }

    @Test
    void getNumberOfEmployeesInHolidays_shouldReturnNumberOfPeopleInHolidays() throws Exception {
        company.addEmployee(vp);
        company.addEmployee(eng);
        company.addEmployee(manager);
        company.addEmployee(intern1);
        company.addEmployee(intern2);

        company.takeHoliday(vp, 2);
        company.takeHoliday(vp, 2);
        company.takeHoliday(eng, 2);
        company.takeHoliday(manager, 2);

        long x = company.getNumberOfEmployeesInHolidays();
        assertThat(x).isEqualTo(3);
    }
}