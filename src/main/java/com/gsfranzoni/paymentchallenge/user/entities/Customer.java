package com.gsfranzoni.paymentchallenge.user.entities;

import com.gsfranzoni.paymentchallenge.user.valueobjects.CPF;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    @Embedded
    @Column(unique = true, name = "cpf")
    private CPF cpf;
}
