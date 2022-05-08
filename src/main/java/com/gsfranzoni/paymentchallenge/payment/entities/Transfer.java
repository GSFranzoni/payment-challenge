package com.gsfranzoni.paymentchallenge.payment.entities;

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
public class Transfer {
    @Id
    private String id;
    @OneToOne(optional = false)
    @JoinColumn(name = "debit_transaction_id")
    private Transaction debitTransaction;
    @OneToOne(optional = false)
    @JoinColumn(name = "credit_transaction_id")
    private Transaction creditTransaction;
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
}
