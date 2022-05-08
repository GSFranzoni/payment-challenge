package com.gsfranzoni.paymentchallenge.payment.exceptions;

public class ShopkeeperCannotSendTransferException extends RuntimeException {
    public ShopkeeperCannotSendTransferException() {
        super("Shopkeeper cannot send transfer");
    }
}
