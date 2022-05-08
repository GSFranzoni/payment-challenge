package com.gsfranzoni.paymentchallenge.payment.repositories;

import com.gsfranzoni.paymentchallenge.payment.entities.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawRepository extends JpaRepository<Withdraw, String> {
}
