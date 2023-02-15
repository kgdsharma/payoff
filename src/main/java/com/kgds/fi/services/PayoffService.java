package com.kgds.fi.services;

import com.kgds.fi.model.AmortizationSchedule;
import com.kgds.fi.model.LoanAccount;
import com.kgds.fi.model.PayOff;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class PayoffService {
    private DateFormat extendedDateFormat;

    public PayOff calculatePayoffQuote(LoanAccount loanAccount, Date payOffQuoteDate) {
        //calculate payoff quote for a given date based on principle remaining and cycle start day
        double remainingPrincipal = loanAccount.getPrincipalRemaining();
        //daily interest
        double dailyInterestRate = loanAccount.getInterestRate() / 36500.0;
        //calculate payoff amount
        double payOffAmount = remainingPrincipal +
                (remainingPrincipal * dailyInterestRate * daysBetweenCycleStartDateAndPayoffQuoteDate(
                        loanAccount.getDayCycleStarts(), payOffQuoteDate.getDate()));

        return PayOff.builder()
                .payoffAmount(payOffAmount)
                .accountNumber(loanAccount.getAccountNumber())
                .payoffDate(extendedDateFormat.format(payOffQuoteDate))
                .build();
    }

    public List<AmortizationSchedule> amortizationSchedule(LoanAccount loanAccount) {
        //calculate amortization schedule for a given loan account
        List amortizationSchedule = new ArrayList();

        double loanAmount = loanAccount.getLoanAmount();
        double interestRate = loanAccount.getInterestRate();
        int loanPeriodInMonths = loanAccount.getLoanTermInYears() * 12;
        double monthlyInterestRate = interestRate / 1200.0;
        double monthlyPayment = loanAmount * monthlyInterestRate /
                (1 - 1 / Math.pow(1 + monthlyInterestRate, loanPeriodInMonths));
        double remainingBalance = loanAmount;
        double PaymentAmount = monthlyPayment;

        for (int i = 0; i < loanPeriodInMonths; i++) {
            double interest = remainingBalance * monthlyInterestRate;
            double principal = monthlyPayment - interest;
            remainingBalance -= principal;
            amortizationSchedule.add(new AmortizationSchedule(i + 1,
                    monthlyPayment, principal, interest, remainingBalance));
        }

        return amortizationSchedule;
    }

    private int daysBetweenCycleStartDateAndPayoffQuoteDate(int cycleStartDD, int payOffQuoteDD) {
        if (cycleStartDD > payOffQuoteDD) {
            return (30 - cycleStartDD) + payOffQuoteDD;
        } else {
            return payOffQuoteDD - cycleStartDD;
        }

    }
}




