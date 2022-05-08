package com.gsfranzoni.paymentchallenge.user.repositories;

import com.gsfranzoni.paymentchallenge.user.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
