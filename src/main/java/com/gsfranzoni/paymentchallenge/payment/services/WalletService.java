package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.entities.Wallet;
import com.gsfranzoni.paymentchallenge.payment.exceptions.InsufficientBalanceException;
import com.gsfranzoni.paymentchallenge.payment.exceptions.InvalidTransactionAmountProvided;
import com.gsfranzoni.paymentchallenge.payment.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Wallet debitAmountFromWallet(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().subtract(amount));
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionAmountProvided(amount);
        }
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(wallet);
        }
        return this.walletRepository.save(wallet);
    }

    public Wallet creditAmountFromWallet(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().add(amount));
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionAmountProvided(amount);
        }
        return this.walletRepository.save(wallet);
    }
}
