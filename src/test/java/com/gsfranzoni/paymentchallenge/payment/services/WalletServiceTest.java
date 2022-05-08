package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.entities.Wallet;
import com.gsfranzoni.paymentchallenge.payment.exceptions.InsufficientBalanceException;
import com.gsfranzoni.paymentchallenge.payment.exceptions.InvalidTransactionAmountProvided;
import com.gsfranzoni.paymentchallenge.payment.repositories.WalletRepository;
import com.gsfranzoni.paymentchallenge.payment.services.WalletService;
import com.gsfranzoni.paymentchallenge.user.entities.Customer;
import com.gsfranzoni.paymentchallenge.user.entities.User;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    WalletService underTest;

    @Test
    void shouldDebitAmountFromWalletSuccessfully() {
        // Given
        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID().toString())
                .balance(new BigDecimal("100.00"))
                .transactions(List.of())
                .build();
        BigDecimal amount = new BigDecimal("10.00");
        // When
        this.underTest.debitAmountFromWallet(wallet, amount);
        // Then
        BigDecimal expectedBalance = new BigDecimal("90.00");
        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        assertThat(wallet.getBalance()).isEqualTo(expectedBalance);
        verify(this.walletRepository, times(1)).save(walletCaptor.capture());
        assertThat(wallet).isEqualTo(walletCaptor.getValue());
    }

    @Test
    void shouldNotDebitAmountFromWalletIfBalanceIsInsufficient() {
        // Given
        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID().toString())
                .balance(new BigDecimal("100.00"))
                .transactions(List.of())
                .build();
        BigDecimal amount = new BigDecimal("100.01");
        // When
        ThrowableAssert.ThrowingCallable when = () -> {
            this.underTest.debitAmountFromWallet(wallet, amount);
        };
        // Then
        assertThatThrownBy(when)
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Insufficient balance to debit amount from wallet with id: " + wallet.getId());
        verify(this.walletRepository, never()).save(any());
    }

    @Test
    void shouldNotDebitNegativeAmountsFromWallet() {
        // Given
        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID().toString())
                .balance(new BigDecimal("100.00"))
                .transactions(List.of())
                .build();
        BigDecimal amount = new BigDecimal("-0.01");
        // When
        ThrowableAssert.ThrowingCallable when = () -> {
            this.underTest.debitAmountFromWallet(wallet, amount);
        };
        // Then
        assertThatThrownBy(when)
                .isInstanceOf(InvalidTransactionAmountProvided.class)
                .hasMessageContaining("Invalid transaction amount provided: " + amount);
        verify(this.walletRepository, never()).save(any());
    }

    @Test
    void shouldCreditAmountFromWalletSuccessfully() {
        // Given
        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID().toString())
                .balance(new BigDecimal("90.00"))
                .transactions(List.of())
                .build();
        BigDecimal amount = new BigDecimal("0.11");
        // When
        this.underTest.creditAmountFromWallet(wallet, amount);
        // Then
        BigDecimal expectedBalance = new BigDecimal("90.11");
        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        assertThat(wallet.getBalance()).isEqualTo(expectedBalance);
        verify(this.walletRepository, times(1)).save(walletCaptor.capture());
        assertThat(wallet).isEqualTo(walletCaptor.getValue());
    }

    @Test
    void shouldNotCreditNegativeAmountsFromWallet() {
        // Given
        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID().toString())
                .balance(new BigDecimal("90.00"))
                .transactions(List.of())
                .build();
        BigDecimal amount = new BigDecimal("-0.11");
        // When
        ThrowableAssert.ThrowingCallable when = () -> {
            this.underTest.creditAmountFromWallet(wallet, amount);
        };
        // Then
        assertThatThrownBy(when)
                .isInstanceOf(InvalidTransactionAmountProvided.class)
                .hasMessageContaining("Invalid transaction amount provided: " + amount);
        verify(this.walletRepository, never()).save(any());
    }
}