package com.gsfranzoni.paymentchallenge.user.dtos;

import com.gsfranzoni.paymentchallenge.user.valueobjects.CNPJ;

public record CreateUserWithCnpjDTO(
        String name,
        String email,
        String password,
        CNPJ cnpj
) {
}
