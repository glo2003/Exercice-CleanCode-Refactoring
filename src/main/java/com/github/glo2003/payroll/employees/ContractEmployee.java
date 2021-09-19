package com.github.glo2003.payroll.employees;

import com.github.glo2003.payroll.Role;
import com.github.glo2003.payroll.exceptions.NotEnoughHolidaysRemainingException;

import java.util.List;

public class ContractEmployee extends Employee {
    private int currentMilestone;
    private final List<Float> payouts;

    public ContractEmployee(String name, Role role, List<Float> payouts) {
        super(name, role, 0);
        this.currentMilestone = 0;
        this.payouts = payouts;
    }

    @Override
    public float getPayForTwoWeeks() {
        if (currentMilestone < payouts.size()) {
            int milestone = currentMilestone;
            currentMilestone += 1;
            return payouts.get(milestone);
        }
        return 0;
    }

    @Override
    public void giveRaise(float raise) {
        if (currentMilestone < payouts.size()) {
            float newAmount = payouts.get(currentMilestone) + raise;
            payouts.set(currentMilestone, newAmount);
        }
    }

    @Override
    public float takePayout() throws NotEnoughHolidaysRemainingException {
        return 0;
    }
}
