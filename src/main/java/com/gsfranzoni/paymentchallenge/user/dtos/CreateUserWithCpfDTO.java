package com.gsfranzoni.paymentchallenge.user.dtos;

import com.gsfranzoni.paymentchallenge.user.valueobjects.CPF;

public record CreateUserWithCpfDTO(
        String name,
        String email,
        String password,
        CPF cpf
) {
}
