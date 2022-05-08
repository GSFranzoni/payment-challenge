package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransactionDTO;
import com.gsfranzoni.paymentchallenge.payment.entities.Transaction;
import com.gsfranzoni.paymentchallenge.payment.entities.Wallet;
import com.gsfranzoni.paymentchallenge.payment.enums.TransactionType;
import com.gsfranzoni.paymentchallenge.payment.exceptions.InvalidTransactionAmountProvided;
import com.gsfranzoni.paymentchallenge.payment.repositories.TransactionRepository;
import com.gsfranzoni.paymentchallenge.payment.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;

    public Transaction createDebitTransaction(CreateTransactionDTO createTransactionDTO) {
        Wallet wallet = walletRepository.findById(createTransactionDTO.walletID())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
        if (createTransactionDTO.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionAmountProvided(createTransactionDTO.amount());
        }
        this.walletService.debitAmountFromWallet(wallet, createTransactionDTO.amount());
        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .wallet(wallet)
                .amount(createTransactionDTO.amount())
                .type(TransactionType.DEBIT)
                .build();
        return this.transactionRepository.save(transaction);
    }

    public Transaction createCreditTransaction(CreateTransactionDTO createTransactionDTO) {
        Wallet wallet = walletRepository.findById(createTransactionDTO.walletID())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
        if (createTransactionDTO.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionAmountProvided(createTransactionDTO.amount());
        }
        this.walletService.creditAmountFromWallet(wallet, createTransactionDTO.amount());
        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .wallet(wallet)
                .amount(createTransactionDTO.amount())
                .type(TransactionType.CREDIT)
                .build();
        return this.transactionRepository.save(transaction);
    }
}
