package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransactionDTO;
import com.gsfranzoni.paymentchallenge.payment.entities.Deposit;
import com.gsfranzoni.paymentchallenge.payment.entities.Transaction;
import com.gsfranzoni.paymentchallenge.payment.repositories.DepositRepository;
import com.gsfranzoni.paymentchallenge.user.entities.User;
import com.gsfranzoni.paymentchallenge.user.exceptions.UserNotFoundException;
import com.gsfranzoni.paymentchallenge.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    public Deposit createDeposit(String userId, BigDecimal amount) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Transaction transaction = this.transactionService.createCreditTransaction(new CreateTransactionDTO(
                user.getWallet().getId(),
                amount
        ));
        Deposit deposit = Deposit.builder()
                .id(UUID.randomUUID().toString())
                .amount(amount)
                .transaction(transaction)
                .build();
        return this.depositRepository.save(deposit);
    }
}
