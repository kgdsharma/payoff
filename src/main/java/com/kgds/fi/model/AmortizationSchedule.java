package com.kgds.fi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class AmortizationSchedule {
    private int paymentNumber;
    private double monthlyPaymentAmount;
    private double monthlyPrincipal;
    private double monthlyInterest;
    private double remainingPrincipal;
}


