package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransactionDTO;
import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransferDTO;
import com.gsfranzoni.paymentchallenge.payment.entities.Transaction;
import com.gsfranzoni.paymentchallenge.payment.entities.Transfer;
import com.gsfranzoni.paymentchallenge.payment.entities.Wallet;
import com.gsfranzoni.paymentchallenge.payment.enums.TransactionType;
import com.gsfranzoni.paymentchallenge.payment.exceptions.ShopkeeperCannotSendTransferException;
import com.gsfranzoni.paymentchallenge.payment.exceptions.InvalidTransferException;
import com.gsfranzoni.paymentchallenge.payment.repositories.TransferRepository;
import com.gsfranzoni.paymentchallenge.user.entities.Customer;
import com.gsfranzoni.paymentchallenge.user.entities.Shopkeeper;
import com.gsfranzoni.paymentchallenge.user.entities.User;
import com.gsfranzoni.paymentchallenge.user.exceptions.UserNotFoundException;
import com.gsfranzoni.paymentchallenge.user.repositories.UserRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransferValidationService transferValidationService;

    @InjectMocks
    TransferService underTest;

    @Test
    void shouldCreateTransferSuccessfully() {
        //given
        CreateTransferDTO input = new CreateTransferDTO(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new BigDecimal("100.00")
        );
        User sourceUser = Customer.builder()
                .id(input.sourceUserId())
                .wallet(Wallet.builder()
                        .id(UUID.randomUUID().toString())
                        .balance(new BigDecimal("100.00"))
                        .transactions(List.of())
                        .build())
                .build();
        User targetUser = Customer.builder()
                .id(input.sourceUserId())
                .wallet(Wallet.builder()
                        .id(UUID.randomUUID().toString())
                        .balance(new BigDecimal("200.00"))
                        .transactions(List.of())
                        .build())
                .build();
        Transaction creditTransaction = Transaction
                .builder()
                .amount(input.amount())
                .wallet(sourceUser.getWallet())
                .type(TransactionType.CREDIT)
                .id(UUID.randomUUID().toString())
                .build();
        Transaction debitTransaction = Transaction
                .builder()
                .amount(input.amount())
                .wallet(targetUser.getWallet())
                .type(TransactionType.DEBIT)
                .id(UUID.randomUUID().toString())
                .build();
        Transfer transfer = Transfer.builder()
                .id(UUID.randomUUID().toString())
                .build();
        given(this.transactionService.createDebitTransaction(any())).willReturn(debitTransaction);
        given(this.transactionService.createCreditTransaction(any())).willReturn(creditTransaction);
        given(this.transferRepository.save(any())).willReturn(transfer);
        given(this.userRepository.findById(input.sourceUserId())).willReturn(Optional.of(sourceUser));
        given(this.userRepository.findById(input.targetUserId())).willReturn(Optional.of(targetUser));
        given(this.transferValidationService.test(any())).willReturn(true);
        //when
        this.underTest.createTransfer(input);
        //then
        ArgumentCaptor<CreateTransactionDTO> debitTransactionCaptor = ArgumentCaptor.forClass(CreateTransactionDTO.class);
        ArgumentCaptor<CreateTransactionDTO> creditTransactionCaptor = ArgumentCaptor.forClass(CreateTransactionDTO.class);
        verify(this.transactionService, times(1)).createDebitTransaction(debitTransactionCaptor.capture());
        verify(this.transactionService, times(1)).createCreditTransaction(creditTransactionCaptor.capture());
        CreateTransactionDTO debitTransactionDTO = debitTransactionCaptor.getValue();
        assertThat(debitTransactionDTO.amount()).isEqualTo(input.amount());
        assertThat(debitTransactionDTO.walletID()).isEqualTo(sourceUser.getWallet().getId());
        CreateTransactionDTO creditTransactionDTO = creditTransactionCaptor.getValue();
        assertThat(creditTransactionDTO.amount()).isEqualTo(input.amount());
        assertThat(creditTransactionDTO.walletID()).isEqualTo(targetUser.getWallet().getId());
        //then
        ArgumentCaptor<Transfer> transferCaptor = ArgumentCaptor.forClass(Transfer.class);
        verify(this.transferRepository, times(1)).save(transferCaptor.capture());
        assertThat(transferCaptor.getValue().getAmount()).isEqualTo(input.amount());
        assertThat(transferCaptor.getValue().getDebitTransaction().getId()).isEqualTo(debitTransaction.getId());
        assertThat(transferCaptor.getValue().getCreditTransaction().getId()).isEqualTo(creditTransaction.getId());
        verify(this.transferValidationService, times(1)).test(any());
    }

    @Test
    void shouldNotCreateTransferWhenTransferIsInvalid() {
        //given
        CreateTransferDTO input = new CreateTransferDTO(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new BigDecimal("100.00")
        );
        given(this.transferValidationService.test(any())).willReturn(false);

        //when
        ThrowableAssert.ThrowingCallable when = () -> this.underTest.createTransfer(input);

        //then
        assertThatThrownBy(when)
                .isInstanceOf(InvalidTransferException.class)
                .hasMessageContaining("Transfer is invalid");
        verify(this.transactionService, never()).createDebitTransaction(any());
        verify(this.transactionService, never()).createDebitTransaction(any());
        verify(this.transferRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateTransferIfSourceUserNotExists() {
        //given
        CreateTransferDTO input = new CreateTransferDTO(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new BigDecimal("100.00")
        );
        given(this.userRepository.findById(input.sourceUserId())).willReturn(Optional.empty());
        given(this.transferValidationService.test(any())).willReturn(true);

        //when
        ThrowableAssert.ThrowingCallable when = () -> this.underTest.createTransfer(input);

        //then
        assertThatThrownBy(when)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id " + input.sourceUserId() + " not found");
        verify(this.transactionService, never()).createDebitTransaction(any());
        verify(this.transactionService, never()).createDebitTransaction(any());
        verify(this.transferRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateTransferIfTargetUserNotExists() {
        //given
        CreateTransferDTO input = new CreateTransferDTO(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new BigDecimal("100.00")
        );
        User sourceUser = Customer.builder()
                .id(input.sourceUserId())
                .build();
        given(this.userRepository.findById(input.sourceUserId())).willReturn(Optional.of(sourceUser));
        given(this.userRepository.findById(input.targetUserId())).willReturn(Optional.empty());
        given(this.transferValidationService.test(any())).willReturn(true);

        //when
        ThrowableAssert.ThrowingCallable when = () -> this.underTest.createTransfer(input);

        //then
        assertThatThrownBy(when)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id " + input.targetUserId() + " not found");
        verify(this.transactionService, never()).createDebitTransaction(any());
        verify(this.transactionService, never()).createCreditTransaction(any());
        verify(this.transferRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateTransferFromShopkeeperUserWallet() {
        //given
        CreateTransferDTO input = new CreateTransferDTO(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new BigDecimal("100.00")
        );
        User sourceUser = Shopkeeper.builder()
                .id(input.sourceUserId())
                .build();
        User targetUser = Customer.builder()
                .id(input.sourceUserId())
                .build();
        given(this.userRepository.findById(input.sourceUserId())).willReturn(Optional.of(sourceUser));
        given(this.userRepository.findById(input.targetUserId())).willReturn(Optional.of(targetUser));
        given(this.transferValidationService.test(any())).willReturn(true);

        //when
        ThrowableAssert.ThrowingCallable when = () -> this.underTest.createTransfer(input);

        //then
        assertThatThrownBy(when)
                .isInstanceOf(ShopkeeperCannotSendTransferException.class)
                .hasMessageContaining("Shopkeeper cannot send transfer");
        verify(this.transactionService, never()).createDebitTransaction(any());
        verify(this.transactionService, never()).createCreditTransaction(any());
        verify(this.transferRepository, never()).save(any());
    }
}