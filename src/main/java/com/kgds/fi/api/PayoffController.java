package com.kgds.fi.api;

import com.google.gson.Gson;
import com.kgds.fi.model.LoanAccount;
import com.kgds.fi.model.PayOff;
import com.kgds.fi.services.AccountService;
import com.kgds.fi.services.LoanPayoffService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@RestController
@AllArgsConstructor
public class PayoffController {
    private AccountService accountService;
    private LoanPayoffService loanPayoffService;
    private DateFormat dateFormat;
    @GetMapping("/account/payoffAmount")
    public ResponseEntity<String> getPayoffAmount(@RequestHeader("accountNumber") String accountNumber, @RequestParam("date") String date) throws Exception {
        LoanAccount loanAccount = accountService.getLoanAccount(accountNumber);
        PayOff payOff = loanPayoffService.calculatePayoffAmount(loanAccount,dateFormat.parse(date));
        return ResponseEntity.ok(new Gson().toJson(payOff));
    }

}
