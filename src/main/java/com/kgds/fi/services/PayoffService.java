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

    public PayOff calculatePayoffAmount(LoanAccount loanAccount, Date date) {
        //calculate payoff amount using daily interest rate and remaining principal amount and the number of days between the given date and the current date
        double dailyInterestRate = loanAccount.getInterestRate() / 365;
        double remainingPrincipal = loanAccount.getPrincipalRemaining();
        long days = (date.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
        double payoffAmount = remainingPrincipal + (1 + dailyInterestRate * days);
        return PayOff.builder()
                .accountNumber(loanAccount.getAccountNumber())
                .payoffAmount(payoffAmount)
                .payoffDate(extendedDateFormat.format(date))
                .build();
    }

    public List<AmortizationSchedule> amortizationSchedule(LoanAccount loanAccount) {
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

}


