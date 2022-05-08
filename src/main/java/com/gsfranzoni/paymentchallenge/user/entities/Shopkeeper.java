package com.gsfranzoni.paymentchallenge.user.entities;

import com.gsfranzoni.paymentchallenge.user.valueobjects.CNPJ;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("SHOPKEEPER")
public class Shopkeeper extends User {
    @Embedded
    @Column(unique = true, name = "cnpj")
    private CNPJ cnpj;
}
