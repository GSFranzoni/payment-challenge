package com.gsfranzoni.paymentchallenge.payment.repositories;

import com.gsfranzoni.paymentchallenge.payment.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
