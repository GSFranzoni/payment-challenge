package com.gsfranzoni.paymentchallenge.payment.exceptions;

import java.math.BigDecimal;

public class InvalidTransactionAmountProvided extends RuntimeException {
    public InvalidTransactionAmountProvided(BigDecimal amount) {
        super(String.format("Invalid transaction amount provided: %s", amount));
    }
}
