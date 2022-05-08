package com.gsfranzoni.paymentchallenge.user.valueobjects;

import com.gsfranzoni.paymentchallenge.user.validations.CnpjValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class CNPJ {

    private String cnpj;

    @Transient
    private CnpjValidation validation;

    public CNPJ(String cnpj) {
        this.validation = new CnpjValidation();
        if (!this.validation.test(cnpj)) {
            throw new IllegalArgumentException("Invalid cnpj provided");
        }
        this.cnpj = cnpj;
    }
}
