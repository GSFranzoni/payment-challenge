package com.gsfranzoni.paymentchallenge.payment.repositories;

import com.gsfranzoni.paymentchallenge.payment.entities.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, String> {
}
