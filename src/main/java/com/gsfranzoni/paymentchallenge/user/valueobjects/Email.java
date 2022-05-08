package com.gsfranzoni.paymentchallenge.user.valueobjects;

import com.gsfranzoni.paymentchallenge.user.validations.EmailValidation;
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
public class Email {

    private String email;

    @Transient
    @Autowired
    private EmailValidation validation;

    public Email(String email) {
        this.validation = new EmailValidation();
        if (!this.validation.test(email)) {
            throw new IllegalArgumentException("Invalid email provided");
        }
        this.email = email;
    }
}
