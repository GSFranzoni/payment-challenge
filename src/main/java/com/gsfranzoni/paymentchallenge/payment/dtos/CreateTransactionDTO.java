package com.gsfranzoni.paymentchallenge.payment.dtos;

import java.math.BigDecimal;

public record CreateTransactionDTO(
        String walletID,
        BigDecimal amount
) {
}
