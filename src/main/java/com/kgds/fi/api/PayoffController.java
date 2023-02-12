package com.kgds.fi.api;

import com.google.gson.Gson;
import com.kgds.fi.model.LoanAccount;
import com.kgds.fi.model.PayOff;
import com.kgds.fi.services.AccountService;
import com.kgds.fi.services.PayoffService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.util.List;


@RestController
@AllArgsConstructor
public class PayoffController {
    private AccountService accountService;
    private PayoffService payoffService;
    private DateFormat dateFormat;

    @GetMapping("/account/payoffAmount")
    public ResponseEntity<String> getPayoffAmount(@RequestHeader("accountNumber") String accountNumber,
                                                  @RequestParam("date") String date) throws Exception {
        LoanAccount loanAccount = accountService.getLoanAccount(accountNumber);
        PayOff payOff = payoffService.calculatePayoffAmount(loanAccount, dateFormat.parse(date));
        return ResponseEntity.ok(new Gson().toJson(payOff));
    }

    @GetMapping("/account/amortizationSchedule")
    public ResponseEntity<String> getAmortizationSchedule(@RequestHeader("accountNumber") String accountNumber){
        LoanAccount loanAccount = accountService.getLoanAccount(accountNumber);
        List amortizationSchedule = payoffService.amortizationSchedule(loanAccount);
        return ResponseEntity.ok(new Gson().toJson(amortizationSchedule));
    }

}
