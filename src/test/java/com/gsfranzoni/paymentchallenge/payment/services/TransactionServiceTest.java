package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransactionDTO;
import com.gsfranzoni.paymentchallenge.payment.entities.Transaction;
import com.gsfranzoni.paymentchallenge.payment.entities.Wallet;
import com.gsfranzoni.paymentchallenge.payment.enums.TransactionType;
import com.gsfranzoni.paymentchallenge.payment.exceptions.InvalidTransactionAmountProvided;
import com.gsfranzoni.paymentchallenge.payment.repositories.TransactionRepository;
import com.gsfranzoni.paymentchallenge.payment.repositories.WalletRepository;
import com.gsfranzoni.paymentchallenge.payment.services.TransactionService;
import com.gsfranzoni.paymentchallenge.payment.services.WalletService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private TransactionService underTest;

    @Test
    void shouldCreateDebitTransactionSuccessfully() {
        // given
        CreateTransactionDTO input = new CreateTransactionDTO(
                UUID.randomUUID().toString(),
                new BigDecimal("10.00")
        );
        Wallet wallet = Wallet.builder()
                .id(input.walletID())
                .build();
        given(this.walletRepository.findById(input.walletID())).willReturn(Optional.of(wallet));
        // when
        underTest.createDebitTransaction(input);
        // then
        verify(walletService, atMostOnce()).debitAmountFromWallet(wallet, input.amount());
        //then
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, atMostOnce()).save(transactionArgumentCaptor.capture());
        assertThat(transactionArgumentCaptor.getValue().getType()).isEqualTo(TransactionType.DEBIT);
        assertThat(transactionArgumentCaptor.getValue().getAmount()).isEqualTo(input.amount());
        assertThat(transactionArgumentCaptor.getValue().getWallet().getId()).isEqualTo(input.walletID());
    }

    @Test
    void shouldNotCreateDebitTransactionWithNegativeAmount() {
        // given
        CreateTransactionDTO input = new CreateTransactionDTO(
                UUID.randomUUID().toString(),
                new BigDecimal("-0.01")
        );
        Wallet wallet = Wallet.builder()
                .id(input.walletID())
                .build();
        given(this.walletRepository.findById(input.walletID())).willReturn(Optional.of(wallet));
        // when
        ThrowableAssert.ThrowingCallable when = () -> underTest.createDebitTransaction(input);
        // then
        assertThatThrownBy(when)
                .isInstanceOf(InvalidTransactionAmountProvided.class)
                .hasMessage(String.format("Invalid transaction amount provided: %s", input.amount()));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldCreateCreditTransactionSuccessfully() {
        // given
        CreateTransactionDTO input = new CreateTransactionDTO(
                UUID.randomUUID().toString(),
                new BigDecimal("10.00")
        );
        Wallet wallet = Wallet.builder()
                .id(input.walletID())
                .build();
        given(this.walletRepository.findById(input.walletID())).willReturn(Optional.of(wallet));
        // when
        underTest.createCreditTransaction(input);
        // then
        verify(walletService, atMostOnce()).creditAmountFromWallet(wallet, input.amount());
        //then
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, atMostOnce()).save(transactionArgumentCaptor.capture());
        assertThat(transactionArgumentCaptor.getValue().getType()).isEqualTo(TransactionType.CREDIT);
        assertThat(transactionArgumentCaptor.getValue().getAmount()).isEqualTo(input.amount());
        assertThat(transactionArgumentCaptor.getValue().getWallet().getId()).isEqualTo(input.walletID());
    }

    @Test
    void shouldNotCreateCreditTransactionWithNegativeAmount() {
        // given
        CreateTransactionDTO input = new CreateTransactionDTO(
                UUID.randomUUID().toString(),
                new BigDecimal("-0.01")
        );
        Wallet wallet = Wallet.builder()
                .id(input.walletID())
                .build();
        given(this.walletRepository.findById(input.walletID())).willReturn(Optional.of(wallet));
        // when
        ThrowableAssert.ThrowingCallable when = () -> underTest.createCreditTransaction(input);
        // then
        assertThatThrownBy(when)
                .isInstanceOf(InvalidTransactionAmountProvided.class)
                .hasMessage(String.format("Invalid transaction amount provided: %s", input.amount()));
        verify(transactionRepository, never()).save(any());
    }
}