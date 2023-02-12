package com.kgds.fi.model;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class AmortizationSchedule {
    private int paymentNumber;
    private double monthlyPaymentAmount;
    private double monthlyPrincipal;
    private double monthlyInterest;
    private double remainingPrincipal;
}
