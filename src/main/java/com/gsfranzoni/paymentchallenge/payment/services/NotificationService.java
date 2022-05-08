package com.gsfranzoni.paymentchallenge.payment.services;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransferDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {

    @Value("${spring.payment.transfer.notification.api.url}")
    private String transferNotificationApiUrl;

    private static final class TransferNotificationResponse {
        public String message;
    }

    public void notify(CreateTransferDTO transfer) {
        Mono<TransferNotificationResponse> response = WebClient.create()
                .get()
                .uri(this.transferNotificationApiUrl)
                .retrieve()
                .bodyToMono(TransferNotificationResponse.class)
                .retry(3);
        response.subscribe(System.out::println);
    }
}
