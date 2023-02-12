package com.kgds.fi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
public class AmortizationSchedule {
    private int paymentNumber;
    private double monthlyPaymentAmount;
    private double monthlyPrincipal;
    private double monthlyInterest;
    private double remainingPrincipal;
}
