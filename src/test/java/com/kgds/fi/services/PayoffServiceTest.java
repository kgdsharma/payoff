package com.kgds.fi.services;

import com.kgds.fi.model.AmortizationSchedule;
import com.kgds.fi.model.LoanAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Though not strictly needed for these tests as we are not mocking
class PayoffServiceTest {

    private final PayoffService payoffService = new PayoffService();

    @Test
    void testAmortizationSchedule_CalculatesCorrectly_OneYearLoan() {
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setLoanAmount(10000.0);
        loanAccount.setInterestRate(5.0); // Annual interest rate
        loanAccount.setLoanStartDate("01-Jan-2023");
        loanAccount.setLoanEndDate("01-Jan-2024"); // 12 months
        loanAccount.setPrincipalRemaining(10000.0); // Assuming no payments made yet
        loanAccount.setDayCycleStarts(1);

        List<AmortizationSchedule> scheduleList = payoffService.amortizationSchedule(loanAccount);

        assertNotNull(scheduleList);
        assertEquals(12, scheduleList.size(), "Schedule should have 12 entries for a 1-year loan.");

        // For the first payment
        AmortizationSchedule firstPayment = scheduleList.get(0);
        assertEquals(1, firstPayment.getPaymentNumber());
        assertEquals(856.07, firstPayment.getMonthlyPaymentAmount(), 0.01, "Monthly payment amount for first payment");
        assertEquals(41.67, firstPayment.getMonthlyInterest(), 0.01, "Monthly interest for first payment");
        assertEquals(814.40, firstPayment.getMonthlyPrincipal(), 0.01, "Monthly principal for first payment");
        assertEquals(9185.60, firstPayment.getRemainingPrincipal(), 0.01, "Remaining principal after first payment");

        // For the last payment
        AmortizationSchedule lastPayment = scheduleList.get(11);
        assertEquals(12, lastPayment.getPaymentNumber());
        // The remaining principal should be very close to 0.
        assertTrue(lastPayment.getRemainingPrincipal() < 1.0, "Remaining principal after last payment should be close to 0.");

        // Verify total principal paid
        double totalPrincipalPaid = scheduleList.stream().mapToDouble(AmortizationSchedule::getMonthlyPrincipal).sum();
        assertEquals(10000.0, totalPrincipalPaid, 0.01, "Total principal paid should equal loan amount.");
    }

    @Test
    void testAmortizationSchedule_ShortLoanTerm_ThreeMonths() {
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setLoanAmount(3000.0);
        loanAccount.setInterestRate(6.0); // Annual interest rate
        loanAccount.setLoanStartDate("01-Jan-2023");
        loanAccount.setLoanEndDate("01-Apr-2023"); // 3 months
        loanAccount.setPrincipalRemaining(3000.0);
        loanAccount.setDayCycleStarts(1);

        List<AmortizationSchedule> scheduleList = payoffService.amortizationSchedule(loanAccount);

        assertNotNull(scheduleList);
        assertEquals(3, scheduleList.size(), "Schedule should have 3 entries for a 3-month loan.");

        // Recalculate expected values for this scenario
        // P = 3000, i = 0.06 / 12 = 0.005, n = 3
        // M = P * [i(1+i)^n] / [(1+i)^n - 1]
        // (1+i)^n = (1.005)^3 = 1.015075125
        // M = 3000 * [0.005 * 1.015075125] / [1.015075125 - 1]
        // M = 3000 * [0.005075375625] / [0.015075125]
        // M = 3000 * 0.33668053... = 1010.04159...
        double expectedMonthlyPayment = 1010.04; // Rounded for assertion

        // First payment
        AmortizationSchedule firstPayment = scheduleList.get(0);
        assertEquals(1, firstPayment.getPaymentNumber());
        assertEquals(expectedMonthlyPayment, firstPayment.getMonthlyPaymentAmount(), 0.01, "Monthly payment amount for first payment");

        double firstMonthInterest = 3000.0 * (0.06 / 12.0); // 3000 * 0.005 = 15.00
        assertEquals(15.00, firstPayment.getMonthlyInterest(), 0.01, "Monthly interest for first payment");

        double firstMonthPrincipal = expectedMonthlyPayment - firstMonthInterest; // 1010.04 - 15.00 = 995.04
        assertEquals(firstMonthPrincipal, firstPayment.getMonthlyPrincipal(), 0.01, "Monthly principal for first payment");

        double remainingAfterFirst = 3000.0 - firstMonthPrincipal; // 3000 - 995.04 = 2004.96
        assertEquals(remainingAfterFirst, firstPayment.getRemainingPrincipal(), 0.01, "Remaining principal after first payment");

        // Last payment (3rd payment)
        AmortizationSchedule lastPayment = scheduleList.get(2);
        assertEquals(3, lastPayment.getPaymentNumber());
        assertTrue(lastPayment.getRemainingPrincipal() < 1.0, "Remaining principal after last payment should be close to 0.");

        // Verify total principal paid
        double totalPrincipalPaid = scheduleList.stream().mapToDouble(AmortizationSchedule::getMonthlyPrincipal).sum();
        assertEquals(3000.0, totalPrincipalPaid, 0.01, "Total principal paid should equal loan amount for short term loan.");
    }
}
