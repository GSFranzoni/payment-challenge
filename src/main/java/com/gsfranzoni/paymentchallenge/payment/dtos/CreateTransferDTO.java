package com.gsfranzoni.paymentchallenge.payment.dtos;

import java.math.BigDecimal;

public record CreateTransferDTO(
        String sourceUserId,
        String targetUserId,
        BigDecimal amount
) {
}
