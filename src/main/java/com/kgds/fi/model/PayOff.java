package com.kgds.fi.model;

import lombok.Builder;

@Builder
public class PayOff {
    private String accountNumber;
    private double payoffAmount;
    private String payoffDate;

}
