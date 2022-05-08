package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransactionDTO;
import com.gsfranzoni.paymentchallenge.payment.entities.Transaction;
import com.gsfranzoni.paymentchallenge.payment.entities.Withdraw;
import com.gsfranzoni.paymentchallenge.payment.enums.TransactionType;
import com.gsfranzoni.paymentchallenge.payment.repositories.WithdrawRepository;
import com.gsfranzoni.paymentchallenge.user.entities.User;
import com.gsfranzoni.paymentchallenge.user.exceptions.UserNotFoundException;
import com.gsfranzoni.paymentchallenge.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WithdrawService {

    @Autowired
    private WithdrawRepository withdrawRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    public Withdraw createWithdraw(String userId, BigDecimal amount) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Transaction transaction = this.transactionService.createDebitTransaction(new CreateTransactionDTO(
                user.getWallet().getId(),
                amount
        ));
        Withdraw withdraw = Withdraw.builder()
                .id(UUID.randomUUID().toString())
                .amount(amount)
                .transaction(transaction)
                .build();
        return this.withdrawRepository.save(withdraw);
    }
}
