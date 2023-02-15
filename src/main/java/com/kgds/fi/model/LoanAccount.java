package com.kgds.fi.model;

import lombok.Data;

@Data
public class LoanAccount {
    //account number
    private String accountNumber;
    // 1. get loan amount
    private double loanAmount;

    // 2. get interest rate
    private double interestRate;
    // 3. get loan term
    private int loanTermInYears;
    // 4. get principal remaining
    private double principalRemaining;
    //loan start date
    private String loanStartDate;
    private String loanEndDate;
    //loan type
    private String accountType;
    //cycle date
    private int dayCycleStarts;

}
