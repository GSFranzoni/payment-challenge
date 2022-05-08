package com.gsfranzoni.paymentchallenge.user.repositories;

import com.gsfranzoni.paymentchallenge.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
