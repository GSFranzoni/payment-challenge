package com.gsfranzoni.paymentchallenge.payment.requests;

import java.math.BigDecimal;

public record TransferRequest(
        String sourceUserId,
        String targetUserId,
        BigDecimal amount
)
{
}
