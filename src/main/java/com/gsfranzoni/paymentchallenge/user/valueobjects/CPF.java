package com.gsfranzoni.paymentchallenge.user.valueobjects;

import com.gsfranzoni.paymentchallenge.user.validations.CpfValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class CPF {

    private String cpf;

    @Transient
    private CpfValidation validation;

    public CPF(String cpf) {
        this.validation = new CpfValidation();
        if (!this.validation.test(cpf)) {
            throw new IllegalArgumentException("Invalid cpf provided");
        }
        this.cpf = cpf;
    }
}
