package com.project.productconsumer;

import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ProductMailConsumer {

    private final KafkaMailFailService kafkaMailFailService;

    public ProductMailConsumer(KafkaMailFailService kafkaMailFailService) {
        this.kafkaMailFailService = kafkaMailFailService;
    }


    @KafkaListener(topics = "${spring.kafka.topics.mail}", groupId = "mailproduct")
    @RetryableTopic(
            attempts = "2",
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    public void listen(String message) {

        System.out.println(message);

    }


    @DltHandler
    public void handleDltPayment(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println();

    }
}
