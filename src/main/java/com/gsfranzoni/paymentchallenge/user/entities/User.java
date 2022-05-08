package com.gsfranzoni.paymentchallenge.user.entities;

import com.gsfranzoni.paymentchallenge.payment.entities.Wallet;
import com.gsfranzoni.paymentchallenge.user.valueobjects.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
abstract public class User {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Embedded
    @Column(unique = true, nullable = false)
    private Email email;
    private String password;
    @OneToOne(optional = false)
    private Wallet wallet;
}
