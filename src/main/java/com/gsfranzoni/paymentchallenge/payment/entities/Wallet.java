package com.gsfranzoni.paymentchallenge.payment.entities;

import com.gsfranzoni.paymentchallenge.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Wallet {
    @Id
    private String id;
    @Column(precision = 10, scale = 2)
    private BigDecimal balance;
    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions;
}
