package com.gsfranzoni.paymentchallenge.payment.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Deposit {
    @Id
    private String id;
    @OneToOne(optional = false)
    private Transaction transaction;
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
}
