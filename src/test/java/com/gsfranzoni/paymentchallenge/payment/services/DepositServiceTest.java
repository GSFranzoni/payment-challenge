package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransactionDTO;
import com.gsfranzoni.paymentchallenge.payment.entities.Deposit;
import com.gsfranzoni.paymentchallenge.payment.entities.Transaction;
import com.gsfranzoni.paymentchallenge.payment.entities.Wallet;
import com.gsfranzoni.paymentchallenge.payment.enums.TransactionType;
import com.gsfranzoni.paymentchallenge.payment.repositories.DepositRepository;
import com.gsfranzoni.paymentchallenge.payment.services.DepositService;
import com.gsfranzoni.paymentchallenge.payment.services.TransactionService;
import com.gsfranzoni.paymentchallenge.user.entities.Customer;
import com.gsfranzoni.paymentchallenge.user.entities.User;
import com.gsfranzoni.paymentchallenge.user.exceptions.UserNotFoundException;
import com.gsfranzoni.paymentchallenge.user.repositories.UserRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {

    @Mock
    DepositRepository depositRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TransactionService transactionService;

    @InjectMocks
    DepositService depositService;

    @Test
    void shouldCreateDepositSuccessfully() {
        //given
        User user = Customer.builder()
                .wallet(Wallet.builder()
                        .id(UUID.randomUUID().toString())
                        .balance(new BigDecimal("100.00"))
                        .build())
                .id(UUID.randomUUID().toString())
                .build();
        BigDecimal amount = new BigDecimal("10.00");
        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .amount(amount)
                .type(TransactionType.CREDIT)
                .wallet(user.getWallet())
                .build();
        given(this.transactionService.createCreditTransaction(any())).willReturn(transaction);
        given(this.userRepository.findById(user.getId())).willReturn(Optional.of(user));
        //when
        this.depositService.createDeposit(user.getId(), amount);
        //then
        ArgumentCaptor<CreateTransactionDTO> transactionDTOArgumentCaptor = ArgumentCaptor.forClass(CreateTransactionDTO.class);
        ArgumentCaptor<Deposit> depositArgumentCaptor = ArgumentCaptor.forClass(Deposit.class);
        verify(this.transactionService, times(1)).createCreditTransaction(transactionDTOArgumentCaptor.capture());
        verify(this.depositRepository, times(1)).save(depositArgumentCaptor.capture());
        CreateTransactionDTO createTransactionDTO = transactionDTOArgumentCaptor.getValue();
        assertThat(createTransactionDTO.amount()).isEqualTo(amount);
        assertThat(createTransactionDTO.walletID()).isEqualTo(user.getWallet().getId());
        Deposit deposit = depositArgumentCaptor.getValue();
        assertThat(deposit.getAmount()).isEqualTo(amount);
        assertThat(deposit.getTransaction()).isEqualTo(transaction);
        assertThat(deposit.getTransaction().getWallet()).isEqualTo(user.getWallet());
    }

    @Test
    void shouldNotCreateDepositIfUserNotExists() {
        //given
        User user = Customer.builder()
                .wallet(Wallet.builder()
                        .id(UUID.randomUUID().toString())
                        .balance(new BigDecimal("100.00"))
                        .build())
                .id(UUID.randomUUID().toString())
                .build();
        BigDecimal amount = new BigDecimal("10.00");
        given(this.userRepository.findById(user.getId())).willReturn(Optional.empty());
        //when
        ThrowableAssert.ThrowingCallable when = () -> this.depositService.createDeposit(user.getId(), amount);
        //then
        assertThatThrownBy(when)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}