package com.github.glo2003.payroll;

import com.github.glo2003.payroll.employees.Employee;
import com.github.glo2003.payroll.exceptions.EmployeeDoesNotWorkHereException;
import com.github.glo2003.payroll.exceptions.InvalidRaiseException;
import com.github.glo2003.payroll.exceptions.NoEmployeeException;
import com.github.glo2003.payroll.exceptions.NotEnoughHolidaysRemainingException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyPayroll {
    final private List<Employee> employees;
    private final List<Paycheck> pendingPaychecks;

    public CompanyPayroll() {
        employees = new ArrayList<>();
        pendingPaychecks = new ArrayList<>();
    }

    public void processPendingPaychecks() {
        for (Employee e : employees) {
            e.resetHasTakenHolidays();
        }
        for (Paycheck p : pendingPaychecks) {
            System.out.println("Sending " + p.getAmount() + "$ to " + p.getTo());
        }
        pendingPaychecks.clear();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public List<Employee> findVicePresidents() {
        return findByRole(Role.VICE_PRESIDENT);
    }

    public List<Employee> findManagers() {
        return findByRole(Role.MANAGER);
    }

    public List<Employee> findEngineers() {
        return findByRole(Role.ENGINEER);
    }

    public List<Employee> findInterns() {
        return findByRole(Role.INTERN);
    }

    private List<Employee> findByRole(Role role) {
        return employees.stream()
                .filter(e -> e.getRole().equals(role))
                .collect(Collectors.toList());
    }

    public void listEmployees() {
        List<Employee> vicePresidents = findVicePresidents();
        System.out.println("Vice presidents:");
        vicePresidents.forEach(e -> System.out.println("\t" + e.toString()));

        List<Employee> engineers = findEngineers();
        System.out.println("Software Engineers:");
        engineers.forEach(e -> System.out.println("\t" + e.toString()));

        List<Employee> managers = findManagers();
        System.out.println("Managers:");
        managers.forEach(e -> System.out.println("\t" + e.toString()));

        List<Employee> interns = findInterns();
        System.out.println("Interns:");
        interns.forEach(e -> System.out.println("\t" + e.toString()));
    }

    public void createPendingPaychecks() {
        for (Employee e : employees) {
            pendingPaychecks.add(new Paycheck(e.getName(), e.getPayForTwoWeeks()));
        }
    }

    public void giveRaise(Employee e, float raise) throws InvalidRaiseException, EmployeeDoesNotWorkHereException {
        if (raise < 0) {
            throw new InvalidRaiseException(raise);
        }
        if (!employees.contains(e)) {
            throw new EmployeeDoesNotWorkHereException(e);
        }
        e.giveRaise(raise);
    }

    public void takeHoliday(Employee e, int amount) throws EmployeeDoesNotWorkHereException, NotEnoughHolidaysRemainingException {
        if (!employees.contains(e)) {
            throw new EmployeeDoesNotWorkHereException(e);
        }
        e.takeHoliday(amount);
    }

    public void takePayout(Employee e) throws EmployeeDoesNotWorkHereException, NotEnoughHolidaysRemainingException {
        if (!employees.contains(e)) {
            throw new EmployeeDoesNotWorkHereException(e);
        }
        Paycheck paycheck = new Paycheck(e.getName(), e.takePayout());
        pendingPaychecks.add(paycheck);
    }

    public float getAveragePendingPaycheck() throws Exception {
        if (pendingPaychecks.size() == 0) {
            throw new NoEmployeeException();
        }
        float totalMoney = getTotalMoney();
        return totalMoney / pendingPaychecks.size();
    }

    public float getTotalMoney() {
        return pendingPaychecks.stream()
                .map(Paycheck::getAmount)
                .reduce(0.f, Float::sum);
    }

    public long getNumberOfEmployeesInHolidays() {
        return employees.stream().filter(Employee::hasTakenHolidays).count();
    }

    public List<Paycheck> getPendings() {
        return pendingPaychecks;
    }
}
