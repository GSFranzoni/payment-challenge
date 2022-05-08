package com.gsfranzoni.paymentchallenge.payment.controllers;

import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransferDTO;
import com.gsfranzoni.paymentchallenge.payment.entities.Transfer;
import com.gsfranzoni.paymentchallenge.payment.requests.TransferRequest;
import com.gsfranzoni.paymentchallenge.payment.services.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment/transfer")
@AllArgsConstructor
public class TransferController {

    @Autowired
    private final TransferService transferService;

    @PostMapping
    public @ResponseBody ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        try {
            Transfer transfer = this.transferService.createTransfer(new CreateTransferDTO(
                    request.sourceUserId(),
                    request.targetUserId(),
                    request.amount()
            ));
            return ResponseEntity.ok().body("Transfer created successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
