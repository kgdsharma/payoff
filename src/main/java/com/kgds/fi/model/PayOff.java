package com.kgds.fi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PayOff {
    private String accountNumber;
    private double payoffAmount;
    private String payoffDate;

}
