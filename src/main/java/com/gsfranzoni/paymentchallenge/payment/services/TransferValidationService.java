package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransferDTO;
import com.gsfranzoni.paymentchallenge.payment.entities.Transfer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Predicate;

@Service
public class TransferValidationService implements Predicate<CreateTransferDTO> {

    @Value("${spring.payment.transfer.validation.api.url}")
    private String transferValidationApiUrl;

    private static final class TransferValidationResponse {
        public String message;
    }

    @Override
    public boolean test(CreateTransferDTO transfer) {
        ResponseEntity<TransferValidationResponse> response = WebClient.create()
                .get()
                .uri(this.transferValidationApiUrl)
                .retrieve()
                .toEntity(TransferValidationResponse.class)
                .block();
        return response != null && response.getStatusCode().is2xxSuccessful();
    }
}
