package com.kgds.fi.services;

import com.kgds.fi.model.LoanAccount;
import com.kgds.fi.model.PayOff;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;

@Service
@AllArgsConstructor
public class LoanPayoffService {
    private DateFormat extendedDateFormat;

        public PayOff calculatePayoffAmount(LoanAccount loanAccount, Date date){
            //calculate payoff amount using daily interest rate and remaining principal amount and the number of days between the given date and the current date
            double dailyInterestRate = loanAccount.getInterestRate() / 365;
            double remainingPrincipal = loanAccount.getPrincipalRemaining();
            long days = (date.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
            double payoffAmount = remainingPrincipal * (1 + dailyInterestRate * days);
            return PayOff.builder()
                    .accountNumber(loanAccount.getAccountNumber())
                    .payoffAmount(payoffAmount)
                    .payoffDate(extendedDateFormat.format(date))
                    .build();
        }
}

