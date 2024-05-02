package com.vitu.mscheckout.controller;

import com.vitu.mscheckout.model.Payment;
import com.vitu.mscheckout.producer.EventBridgeProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders/event")
public class CheckoutController {

    private final EventBridgeProducer eventBridgeProducer;

    public CheckoutController(EventBridgeProducer eventBridgeProducer) {
        this.eventBridgeProducer = eventBridgeProducer;
    }

    @PostMapping
    public void finishOrder(@RequestBody Payment payment) {
        eventBridgeProducer.finishOrder(payment);
    }
}
