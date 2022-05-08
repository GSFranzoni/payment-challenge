package com.gsfranzoni.paymentchallenge.payment.repositories;

import com.gsfranzoni.paymentchallenge.payment.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, String> {
}
