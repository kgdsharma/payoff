package com.kgds.fi.services;

import com.kgds.fi.model.LoanAccount;
import com.kgds.fi.model.PayOff;
import com.kgds.fi.model.AmortizationSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest; // Not used in current version
// import org.junit.jupiter.params.provider.CsvSource; // Not used in current version

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class PayoffServiceTest {

    private PayoffService payoffService;

    @BeforeEach
    void setUp() {
        payoffService = new PayoffService(); // PayoffService has no dependencies now
    }

    private LoanAccount createLoanAccount(double principalRemaining, double interestRate, int loanTermYears, double loanAmount, int dayCycleStarts) {
        LoanAccount account = new LoanAccount();
        account.setAccountNumber("TestAcc123");
        account.setPrincipalRemaining(principalRemaining);
        account.setInterestRate(interestRate); // Annual interest rate in percentage (e.g., 3.65 for 3.65%)
        account.setLoanTermInYears(loanTermYears);
        account.setLoanAmount(loanAmount);
        account.setDayCycleStarts(dayCycleStarts);
        return account;
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Test
    void testCalculatePayoffQuote_SameMonth_NoLeap() {
        LoanAccount account = createLoanAccount(10000.0, 3.65, 5, 10000.0, 1);
        LocalDate payoffDate = LocalDate.of(2023, 3, 15);
        PayOff payOff = payoffService.calculatePayoffQuote(account, toDate(payoffDate));

        assertEquals("TestAcc123", payOff.getAccountNumber());
        assertEquals(10014.0, payOff.getPayoffAmount(), 0.01);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.US);
        assertEquals(formatter.format(payoffDate), payOff.getPayoffDate());
    }

    @Test
    void testCalculatePayoffQuote_AcrossMonthBoundary_NoLeap() {
        LoanAccount account = createLoanAccount(10000.0, 3.65, 5, 10000.0, 20);
        LocalDate payoffDate = LocalDate.of(2023, 4, 5);
        PayOff payOff = payoffService.calculatePayoffQuote(account, toDate(payoffDate));

        assertEquals(10016.0, payOff.getPayoffAmount(), 0.01);
    }

    @Test
    void testCalculatePayoffQuote_AcrossYearBoundary_LeapToNonLeap() {
        LoanAccount account = createLoanAccount(10000.0, 3.65, 5, 10000.0, 28);
        LocalDate payoffDate = LocalDate.of(2021, 1, 5);
        PayOff payOff = payoffService.calculatePayoffQuote(account, toDate(payoffDate));

        assertEquals(10008.0, payOff.getPayoffAmount(), 0.01);
    }

    @Test
    void testCalculatePayoffQuote_LeapYear_EndOfFeb() {
        LoanAccount account = createLoanAccount(10000.0, 3.66, 5, 10000.0, 1);
        LocalDate payoffDate = LocalDate.of(2024, 2, 29);
        PayOff payOff = payoffService.calculatePayoffQuote(account, toDate(payoffDate));

        assertEquals(10028.0, payOff.getPayoffAmount(), 0.01);
    }

    @Test
    void testCalculatePayoffQuote_CycleStartAfterQuoteDay_PreviousMonthCycle() {
        LoanAccount account = createLoanAccount(5000.0, 7.30, 3, 5000.0, 15);
        LocalDate payoffDate = LocalDate.of(2023, 8, 10);
        PayOff payOff = payoffService.calculatePayoffQuote(account, toDate(payoffDate));

        assertEquals(5026.0, payOff.getPayoffAmount(), 0.01);
    }

    @Test
    void testAmortizationSchedule_StructureAndCounts() {
        LoanAccount account = createLoanAccount(200000.0, 6.0, 1, 200000.0, 1);
        List<AmortizationSchedule> schedule = payoffService.amortizationSchedule(account);

        assertNotNull(schedule);
        assertEquals(12, schedule.size());
    }

    @Test
    void testAmortizationSchedule_FirstAndLastInstallment() {
        LoanAccount account = createLoanAccount(12000.0, 12.0, 1, 12000.0, 1);
        List<AmortizationSchedule> schedule = payoffService.amortizationSchedule(account);

        assertEquals(12, schedule.size());

        AmortizationSchedule first = schedule.get(0);
        assertEquals(1, first.getPaymentNumber());
        assertEquals(1066.19, first.getPaymentAmount(), 0.01);
        assertEquals(120.00, first.getInterestPaid(), 0.01);
        assertEquals(946.19, first.getPrincipalPaid(), 0.01);
        assertEquals(11053.81, first.getRemainingBalance(), 0.01);

        AmortizationSchedule last = schedule.get(11);
        assertEquals(12, last.getPaymentNumber());
        assertEquals(1066.19, last.getPaymentAmount(), 0.01);
        assertTrue(last.getRemainingBalance() < 1.0, "Remaining balance should be close to 0");
        assertEquals(10.56, last.getInterestPaid(), 0.01);
        assertEquals(1055.63, last.getPrincipalPaid(), 0.01);
    }

    @Test
    void testAmortizationSchedule_ZeroInterest() {
        LoanAccount account = createLoanAccount(12000.0, 0.0, 1, 12000.0, 1);
        List<AmortizationSchedule> schedule = payoffService.amortizationSchedule(account);

        assertEquals(12, schedule.size());
        AmortizationSchedule first = schedule.get(0);
        assertEquals(1000.0, first.getPaymentAmount(), 0.01);
        assertEquals(0.0, first.getInterestPaid(), 0.01);
        assertEquals(1000.0, first.getPrincipalPaid(), 0.01);
        assertEquals(11000.0, first.getRemainingBalance(), 0.01);

        AmortizationSchedule last = schedule.get(11);
        assertEquals(1000.0, last.getPaymentAmount(), 0.01);
        assertEquals(0.0, last.getInterestPaid(), 0.01);
        assertEquals(1000.0, last.getPrincipalPaid(), 0.01);
        assertEquals(0.0, last.getRemainingBalance(), 0.01);
    }
}
