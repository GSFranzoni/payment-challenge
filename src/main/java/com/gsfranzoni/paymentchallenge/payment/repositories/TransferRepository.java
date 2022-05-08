package com.gsfranzoni.paymentchallenge.payment.repositories;

import com.gsfranzoni.paymentchallenge.payment.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, String> {
}
