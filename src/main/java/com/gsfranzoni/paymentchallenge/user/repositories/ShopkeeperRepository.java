package com.gsfranzoni.paymentchallenge.user.repositories;

import com.gsfranzoni.paymentchallenge.user.entities.Shopkeeper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopkeeperRepository extends JpaRepository<Shopkeeper, String> {
}
