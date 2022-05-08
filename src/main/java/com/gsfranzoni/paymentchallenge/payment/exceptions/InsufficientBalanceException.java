package com.gsfranzoni.paymentchallenge.payment.exceptions;

import com.gsfranzoni.paymentchallenge.payment.entities.Wallet;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(Wallet wallet) {
        super("Insufficient balance to debit amount from wallet with id: " + wallet.getId());
    }
}
