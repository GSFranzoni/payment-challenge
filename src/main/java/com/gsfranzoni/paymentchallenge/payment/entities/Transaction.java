package com.gsfranzoni.paymentchallenge.payment.entities;

import com.gsfranzoni.paymentchallenge.payment.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Transaction {
    @Id
    private String id;
    @ManyToOne()
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
    private TransactionType type;
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
}
