package com.kgds.fi.services;

import com.kgds.fi.model.LoanAccount;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {PayoffService.class})
@ExtendWith(SpringExtension.class)
class PayoffServiceTest {
    @MockBean
    private DateFormat dateFormat;

    @Autowired
    private PayoffService payoffService;

    /**
     * Method under test: {@link PayoffService#calculatePayoffQuote(LoanAccount, Date)}
     */
    @Test
    void testCalculatePayoffQuote() {
        when(dateFormat.format((Date) any(), (StringBuffer) any(), (FieldPosition) any())).thenReturn(new StringBuffer());

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setAccountNumber("42");
        loanAccount.setAccountType("3");
        loanAccount.setDayCycleStarts(1);
        loanAccount.setInterestRate(10.0d);
        loanAccount.setLoanAmount(10.0d);
        loanAccount.setLoanEndDate("2020-03-01");
        loanAccount.setLoanStartDate("2020-03-01");
        loanAccount.setLoanTermInYears(1);
        loanAccount.setPrincipalRemaining(10.0d);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        payoffService.calculatePayoffQuote(loanAccount,
                Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        verify(dateFormat).format((Date) any(), (StringBuffer) any(), (FieldPosition) any());
    }

    /**
     * Method under test: {@link PayoffService#calculatePayoffQuote(LoanAccount, Date)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCalculatePayoffQuote2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "java.lang.StringBuffer.toString()" because the return value of "java.text.DateFormat.format(java.util.Date, StringBuffer, java.text.FieldPosition)" is null
        //       at java.text.DateFormat.format(DateFormat.java:379)
        //       at com.kgds.fi.services.PayoffService.calculatePayoffQuote(PayoffService.java:32)
        //   See https://diff.blue/R013 to resolve this issue.

        when(dateFormat.format((Date) any(), (StringBuffer) any(), (FieldPosition) any())).thenReturn(null);

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setAccountNumber("42");
        loanAccount.setAccountType("3");
        loanAccount.setDayCycleStarts(1);
        loanAccount.setInterestRate(10.0d);
        loanAccount.setLoanAmount(10.0d);
        loanAccount.setLoanEndDate("2020-03-01");
        loanAccount.setLoanStartDate("2020-03-01");
        loanAccount.setLoanTermInYears(1);
        loanAccount.setPrincipalRemaining(10.0d);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        payoffService.calculatePayoffQuote(loanAccount,
                Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
    }

    /**
     * Method under test: {@link PayoffService#calculatePayoffQuote(LoanAccount, java.util.Date)}
     */
    @Test
    void testCalculatePayoffQuote3() {
        when(dateFormat.format((java.util.Date) any(), (StringBuffer) any(), (FieldPosition) any()))
                .thenReturn(new StringBuffer());

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setAccountNumber("42");
        loanAccount.setAccountType("3");
        loanAccount.setDayCycleStarts(1);
        loanAccount.setInterestRate(10.0d);
        loanAccount.setLoanAmount(10.0d);
        loanAccount.setLoanEndDate("2020-03-01");
        loanAccount.setLoanStartDate("2020-03-01");
        loanAccount.setLoanTermInYears(1);
        loanAccount.setPrincipalRemaining(10.0d);
        java.sql.Date date = mock(java.sql.Date.class);
        when(date.getDate()).thenReturn(-1);
        payoffService.calculatePayoffQuote(loanAccount, date);
        //TODO: Expectation vs Actual.
        verify(dateFormat).format((java.util.Date) any(), (StringBuffer) any(), (FieldPosition) any());
        verify(date).getDate();
    }

    /**
     * Method under test: {@link PayoffService#amortizationSchedule(LoanAccount)}
     */
    @Test
    void testAmortizationSchedule() {
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setAccountNumber("42");
        loanAccount.setAccountType("3");
        loanAccount.setDayCycleStarts(1);
        loanAccount.setInterestRate(10.0d);
        loanAccount.setLoanAmount(10.0d);
        loanAccount.setLoanEndDate("2020-03-01");
        loanAccount.setLoanStartDate("2020-03-01");
        loanAccount.setLoanTermInYears(1);
        loanAccount.setPrincipalRemaining(10.0d);
        assertEquals(12, payoffService.amortizationSchedule(loanAccount).size());
    }

    /**
     * Method under test: {@link PayoffService#amortizationSchedule(LoanAccount)}
     */
    @Test
    void testAmortizationSchedule2() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        when(loanAccount.getInterestRate()).thenReturn(10.0d);
        when(loanAccount.getLoanAmount()).thenReturn(10.0d);
        when(loanAccount.getLoanTermInYears()).thenReturn(1);
        doNothing().when(loanAccount).setAccountNumber((String) any());
        doNothing().when(loanAccount).setAccountType((String) any());
        doNothing().when(loanAccount).setDayCycleStarts(anyInt());
        doNothing().when(loanAccount).setInterestRate(anyDouble());
        doNothing().when(loanAccount).setLoanAmount(anyDouble());
        doNothing().when(loanAccount).setLoanEndDate((String) any());
        doNothing().when(loanAccount).setLoanStartDate((String) any());
        doNothing().when(loanAccount).setLoanTermInYears(anyInt());
        doNothing().when(loanAccount).setPrincipalRemaining(anyDouble());
        loanAccount.setAccountNumber("42");
        loanAccount.setAccountType("3");
        loanAccount.setDayCycleStarts(1);
        loanAccount.setInterestRate(10.0d);
        loanAccount.setLoanAmount(10.0d);
        loanAccount.setLoanEndDate("2020-03-01");
        loanAccount.setLoanStartDate("2020-03-01");
        loanAccount.setLoanTermInYears(1);
        loanAccount.setPrincipalRemaining(10.0d);
        assertEquals(12, payoffService.amortizationSchedule(loanAccount).size());
        verify(loanAccount).getInterestRate();
        verify(loanAccount).getLoanAmount();
        verify(loanAccount).getLoanTermInYears();
        verify(loanAccount).setAccountNumber((String) any());
        verify(loanAccount).setAccountType((String) any());
        verify(loanAccount).setDayCycleStarts(anyInt());
        verify(loanAccount).setInterestRate(anyDouble());
        verify(loanAccount).setLoanAmount(anyDouble());
        verify(loanAccount).setLoanEndDate((String) any());
        verify(loanAccount).setLoanStartDate((String) any());
        verify(loanAccount).setLoanTermInYears(anyInt());
        verify(loanAccount).setPrincipalRemaining(anyDouble());
    }
}

