package com.gsfranzoni.paymentchallenge;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransferDTO;
import com.gsfranzoni.paymentchallenge.payment.entities.Wallet;
import com.gsfranzoni.paymentchallenge.payment.repositories.WalletRepository;
import com.gsfranzoni.paymentchallenge.payment.services.NotificationService;
import com.gsfranzoni.paymentchallenge.payment.services.TransferValidationService;
import com.gsfranzoni.paymentchallenge.user.entities.Customer;
import com.gsfranzoni.paymentchallenge.user.entities.Shopkeeper;
import com.gsfranzoni.paymentchallenge.user.entities.User;
import com.gsfranzoni.paymentchallenge.user.repositories.UserRepository;
import com.gsfranzoni.paymentchallenge.user.valueobjects.CNPJ;
import com.gsfranzoni.paymentchallenge.user.valueobjects.CPF;
import com.gsfranzoni.paymentchallenge.user.valueobjects.Email;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class PaymentChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentChallengeApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(UserRepository userRepository, WalletRepository walletRepository) {
//		return args -> {
//			Wallet wallet1 = walletRepository.save(Wallet.builder()
//					.id(UUID.randomUUID().toString())
//					.balance(new BigDecimal("500.00"))
//					.transactions(List.of())
//					.build());
//			userRepository.save(Customer.builder()
//					.id(UUID.randomUUID().toString())
//					.name("Gui")
//					.email(new Email(UUID.randomUUID().toString()))
//					.cpf(new CPF(UUID.randomUUID().toString()))
//					.password("123")
//					.wallet(wallet1)
//					.build()
//			);
//			Wallet wallet2 = walletRepository.save(Wallet.builder()
//					.id(UUID.randomUUID().toString())
//					.balance(new BigDecimal("0.00"))
//					.transactions(List.of())
//					.build());
//			userRepository.save(Shopkeeper.builder()
//					.id(UUID.randomUUID().toString())
//					.name("Gui")
//					.email(new Email(UUID.randomUUID().toString()))
//					.cnpj(new CNPJ(UUID.randomUUID().toString()))
//					.password("123")
//					.wallet(wallet2)
//					.build()
//			);
//		};
//	}

}
