package com.kgds.fi.services;

import com.kgds.fi.model.LoanAccount;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class AccountService {
    private WebClient webClient;

    //get loan account details from service called through webclient
    public LoanAccount getLoanAccount(String accountNumber) {
        return webClient.get()
                .uri("/accounts/{id}", accountNumber)
                .retrieve()
                .bodyToMono(LoanAccount.class)
                .block();
    }
}
