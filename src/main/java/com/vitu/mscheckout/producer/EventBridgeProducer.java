package com.vitu.mscheckout.producer;

import com.amazonaws.services.eventbridge.AmazonEventBridge;
import com.amazonaws.services.eventbridge.model.PutEventsRequest;
import com.amazonaws.services.eventbridge.model.PutEventsRequestEntry;
import com.amazonaws.services.eventbridge.model.PutEventsResult;
import com.vitu.mscheckout.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventBridgeProducer {

    @Value("${aws.bus.name}")
    private String bus;

    private final Logger logger = LoggerFactory.getLogger(EventBridgeProducer.class);

    private final AmazonEventBridge amazonEventBridge;

    public EventBridgeProducer(AmazonEventBridge amazonEventBridge) {
        this.amazonEventBridge = amazonEventBridge;
    }

    public void finishOrder(Payment payment) {
        PutEventsRequestEntry putEventsRequestEntry = new PutEventsRequestEntry();

        putEventsRequestEntry
                .withSource(payment.origem())
                .withDetail("{\"valor\": \"" + payment.valor() + "\"}")
                .withDetailType(payment.status())
                .withEventBusName(bus);

        final PutEventsRequest putEventsRequest = new PutEventsRequest().withEntries(putEventsRequestEntry);

        final PutEventsResult putEventsResult = amazonEventBridge.putEvents(putEventsRequest);

        logger.info("{}", putEventsResult);

    }
}
