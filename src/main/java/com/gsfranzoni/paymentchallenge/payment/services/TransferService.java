package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransactionDTO;
import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransferDTO;
import com.gsfranzoni.paymentchallenge.payment.entities.Transaction;
import com.gsfranzoni.paymentchallenge.payment.entities.Transfer;
import com.gsfranzoni.paymentchallenge.payment.exceptions.InvalidTransferException;
import com.gsfranzoni.paymentchallenge.payment.exceptions.ShopkeeperCannotSendTransferException;
import com.gsfranzoni.paymentchallenge.payment.repositories.TransferRepository;
import com.gsfranzoni.paymentchallenge.user.entities.Shopkeeper;
import com.gsfranzoni.paymentchallenge.user.entities.User;
import com.gsfranzoni.paymentchallenge.user.exceptions.UserNotFoundException;
import com.gsfranzoni.paymentchallenge.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransferValidationService transferValidationService;

    public Transfer createTransfer(CreateTransferDTO createTransferDTO) {
        if (!this.transferValidationService.test(createTransferDTO)) {
            throw new InvalidTransferException();
        }
        User sourceUser = this.userRepository.findById(createTransferDTO.sourceUserId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + createTransferDTO.sourceUserId() + " not found"));
        User targetUser = this.userRepository.findById(createTransferDTO.targetUserId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + createTransferDTO.targetUserId() + " not found"));
        if (sourceUser instanceof Shopkeeper) {
            throw new ShopkeeperCannotSendTransferException();
        }
        Transaction debitTransaction = this.transactionService.createDebitTransaction(new CreateTransactionDTO(
                sourceUser.getWallet().getId(),
                createTransferDTO.amount()
        ));
        Transaction creditTransaction = this.transactionService.createCreditTransaction(new CreateTransactionDTO(
                targetUser.getWallet().getId(),
                createTransferDTO.amount()
        ));
        Transfer transfer = Transfer.builder()
                .id(UUID.randomUUID().toString())
                .amount(createTransferDTO.amount())
                .debitTransaction(debitTransaction)
                .creditTransaction(creditTransaction)
                .build();
        return this.transferRepository.save(transfer);
    }
}
