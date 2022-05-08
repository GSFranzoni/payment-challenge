package com.gsfranzoni.paymentchallenge.user.validations;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
@NoArgsConstructor
public class CpfValidation implements Predicate<String> {
    @Override
    public boolean test(String email) {
        return true;
    }
}
