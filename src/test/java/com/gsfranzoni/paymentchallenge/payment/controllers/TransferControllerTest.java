package com.gsfranzoni.paymentchallenge.payment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsfranzoni.paymentchallenge.payment.dtos.CreateTransferDTO;
import com.gsfranzoni.paymentchallenge.payment.requests.TransferRequest;
import com.gsfranzoni.paymentchallenge.payment.services.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @Test
    void shouldCreateTransferSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(new TransferRequest(
                "1",
                "2",
                new BigDecimal("10.00")
        ));
        MockHttpServletRequestBuilder request = post("/payment/transfer")
                .accept("application/json")
                .contentType("application/json")
                .content(body);
        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Transfer created successfully")));
    }

    @Test
    void shouldReturnBadRequestWhenThrowingException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        given(this.transferService.createTransfer(any(CreateTransferDTO.class)))
                .willThrow(RuntimeException.class);;
        String body = mapper.writeValueAsString(new TransferRequest(
                "1",
                "2",
                new BigDecimal("10.00")
        ));
        MockHttpServletRequestBuilder request = post("/payment/transfer")
                .accept("application/json")
                .contentType("application/json")
                .content(body);
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}