package com.kgds.fi.services;

import com.kgds.fi.model.AmortizationSchedule;
import com.kgds.fi.model.LoanAccount;
import com.kgds.fi.model.PayOff;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class PayoffService {
    private static final DateTimeFormatter EXTENDED_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.US);

    public PayOff calculatePayoffQuote(LoanAccount loanAccount, Date payOffQuoteDate) {
        //calculate payoff quote for a given date based on principle remaining and cycle start day
        double remainingPrincipal = loanAccount.getPrincipalRemaining();

        LocalDate payOffQuoteLocalDate = payOffQuoteDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int cycleDay = loanAccount.getDayCycleStarts();
        LocalDate currentCycleStartDate = payOffQuoteLocalDate.withDayOfMonth(cycleDay);

        if (payOffQuoteLocalDate.getDayOfMonth() < cycleDay) {
            currentCycleStartDate = payOffQuoteLocalDate.minusMonths(1).withDayOfMonth(cycleDay);
        }

        long daysBetween = ChronoUnit.DAYS.between(currentCycleStartDate, payOffQuoteLocalDate);

        //Consider leap year for daily interest rate
        int daysInYear = payOffQuoteLocalDate.lengthOfYear();
        double dailyInterestRate = loanAccount.getInterestRate() / (daysInYear * 100.0);

        //calculate payoff amount
        double payOffAmount = remainingPrincipal + (remainingPrincipal * dailyInterestRate * daysBetween);

       return PayOff.builder()
                .accountNumber(loanAccount.getAccountNumber())
                .payoffAmount(payOffAmount)
                .payoffDate(EXTENDED_DATE_FORMATTER.format(payOffQuoteLocalDate))
                .build();
    }

    public List<AmortizationSchedule> amortizationSchedule(LoanAccount loanAccount) {
        //calculate amortization schedule for a given loan account
        List<AmortizationSchedule> amortizationSchedule = new ArrayList<AmortizationSchedule>();

        double loanAmount = loanAccount.getLoanAmount();
        double interestRate = loanAccount.getInterestRate();
        int loanPeriodInMonths = loanAccount.getLoanTermInYears() * 12;
        double monthlyInterestRate = interestRate / 1200.0;
        double monthlyPayment = loanAmount * monthlyInterestRate /
                (1 - 1 / Math.pow(1 + monthlyInterestRate, loanPeriodInMonths));
        double remainingBalance = loanAmount;

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