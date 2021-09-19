package com.github.glo2003.payroll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompanyPayroll {
    final private List<Employee> employees;
    private final List<Paycheck> pendingPaychecks;
    private final List<Boolean> isEmployeeTakingHolidays;

    public CompanyPayroll() {
        employees = new ArrayList<>();
        pendingPaychecks = new ArrayList<>();
        isEmployeeTakingHolidays = new ArrayList<>();
    }

    public void processPendingPaychecks() {
        IntStream.range(0, pendingPaychecks.size()).forEach((i) -> isEmployeeTakingHolidays.set(i, false));
        for (Paycheck p : pendingPaychecks) {
            System.out.println("Sending " + p.getAmount() + "$ to " + p.getTo());
        }
        pendingPaychecks.clear();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        isEmployeeTakingHolidays.add(false);
    }

    public List<Employee> findEngineers() {
        return findByRole("engineer");
    }

    public List<Employee> findManagers() {
        return findByRole("manager");
    }

    public List<Employee> findVicePresidents() {
        return findByRole("vp");
    }

    public List<Employee> findInterns() {
        return findByRole("intern");
    }

    private List<Employee> findByRole(String role) {
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
            if (e instanceof HourlyEmployee) {
                HourlyEmployee he = (HourlyEmployee) e;
                pendingPaychecks.add(new Paycheck(e.getName(), he.getWorkedHoursFor2Weeks() * he.getHourlyRate()));
            } else if (e instanceof SalariedEmployee) {
                SalariedEmployee se = (SalariedEmployee) e;
                pendingPaychecks.add(new Paycheck(e.getName(), ((SalariedEmployee) e).getBiweeklySalary()));
            } else {
                throw new RuntimeException("something happened");
            }
        }
    }

    public void giveRaise(Employee e, float raise) {
        if (raise < 0) {
            throw new RuntimeException("oh no");
        }
        if (!employees.contains(e)) {
            throw new RuntimeException("not here");
        }
        if (e instanceof HourlyEmployee) {
            HourlyEmployee he = (HourlyEmployee) e;
            he.setHourlyRate(he.getHourlyRate() + raise);
        } else if (e instanceof SalariedEmployee) {
            SalariedEmployee se = (SalariedEmployee) e;
            se.setBiweeklySalary(se.getBiweeklySalary() + raise);
        } else {
            throw new RuntimeException("something happened");
        }
    }

    public void takeHoliday(Employee e, boolean payout, Integer amount) {
        // TODO this could probably be split in two methods...
        if (!employees.contains(e)) {
            throw new RuntimeException("not here");
        }
        if (payout) {
            if (amount != null) {
                throw new RuntimeException("bad input");
            } else {
                amount = 5;
            }
        } else {
            if (amount == null) {
                throw new RuntimeException("bad input");
            }
        }

        if (!payout && e.getVacationDays() < amount) {
            throw new RuntimeException("error");
        }
        if (e instanceof HourlyEmployee) {
            HourlyEmployee he = (HourlyEmployee) e;
            if (payout) {
                pendingPaychecks.add(new Paycheck(e.getName(), ((HourlyEmployee) e).getWorkedHoursFor2Weeks() * ((HourlyEmployee) e).getHourlyRate() / 2f));
                e.setVacationDays(e.getVacationDays() - amount);
            } else {
                e.setVacationDays(e.getVacationDays() - amount);
            }
        } else if (e instanceof SalariedEmployee) {
            SalariedEmployee se = (SalariedEmployee) e;

            if (payout) {
                pendingPaychecks.add(new Paycheck(e.getName(), ((SalariedEmployee) e).getBiweeklySalary() / 2f));
                e.setVacationDays(e.getVacationDays() - amount);
            } else {
                e.setVacationDays(e.getVacationDays() - amount);
            }
        } else {
            throw new RuntimeException("something happened");
        }

        int i = employees.indexOf(e);
        if (e instanceof HourlyEmployee) {
            if (!isEmployeeTakingHolidays.contains(e))
                isEmployeeTakingHolidays.set(i, true);
        } else if (e instanceof SalariedEmployee) {
            if (!isEmployeeTakingHolidays.contains(e))
                isEmployeeTakingHolidays.set(i, true);
        } else {
            throw new RuntimeException("something happened");
        }
    }

    public float getAveragePendingPaycheck() throws Exception {
        if (pendingPaychecks.size() == 0) {
            throw new Exception("There is no employee");
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
        return isEmployeeTakingHolidays.stream().filter(e -> e).count();
    }

    public List<Paycheck> getPendings() {
        return pendingPaychecks;
    }
}
