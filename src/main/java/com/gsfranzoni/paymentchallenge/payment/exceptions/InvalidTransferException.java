package com.gsfranzoni.paymentchallenge.payment.exceptions;

public class InvalidTransferException extends RuntimeException {
    public InvalidTransferException() {
        super("Transfer is invalid");
    }
}
