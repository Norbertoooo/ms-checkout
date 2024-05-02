package com.vitu.mscheckout.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitu.mscheckout.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    AmazonSQS sqs;

    @BeforeEach
    void setUp() {
    }

    @Test
    void finalizarOrdemAprovada() throws Exception {
        mockMvc.perform(post("/orders/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Payment("internacional", "123", "APROVADO"))))
                .andDo(print())
                .andExpect(status().isCreated());


        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest();
        receiveMessageRequest.setQueueUrl("http://localhost.localstack.cloud:4566/000000000000/ordem-finalizada");
        ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

        Optional<Message> message = receiveMessageResult.getMessages().stream().findFirst();

        assertFalse(message.isEmpty());
        assertTrue(message.get().getBody().contains("APROVADO"));
    }

    @Test
    void finalizarOrdemReprovada() throws Exception {
        mockMvc.perform(post("/orders/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Payment("nacional", "456", "REPROVADO"))))
                .andDo(print())
                .andExpect(status().isCreated());


        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest();
        receiveMessageRequest.setQueueUrl("http://localhost.localstack.cloud:4566/000000000000/ordem-cancelada");
        ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);

        Optional<Message> message = receiveMessageResult.getMessages().stream().findFirst();

        assertFalse(message.isEmpty());
        assertTrue(message.get().getBody().contains("REPROVADO"));
    }
}