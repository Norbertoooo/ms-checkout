package com.vitu.mscheckout.controller;

import com.vitu.mscheckout.model.Payment;
import com.vitu.mscheckout.producer.EventBridgeProducer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders/event")
public class CheckoutController {

    private final EventBridgeProducer eventBridgeProducer;

    public CheckoutController(EventBridgeProducer eventBridgeProducer) {
        this.eventBridgeProducer = eventBridgeProducer;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void finishOrder(@RequestBody Payment payment) {
        eventBridgeProducer.finishOrder(payment);
    }
}
