package com.project.service.kafka.consumer;

import com.project.entity.OrderEntity;
import com.project.enums.OrderState;
import com.project.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductConsumer {

    private final OrderService orderService;


    public ProductConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${spring.topic.stock.update_fail}", groupId = "productUpdate")
    @RetryableTopic(attempts = "1",
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    public void listenfailUpdateStoc(String orderNumber) {

        OrderEntity order = orderService.findOrderByOrderNumber(orderNumber);
        order.setOrderState(OrderState.CANCELED);
        orderService.saveOrder(order);


    }

    @DltHandler
    public void handleDltOrder(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String message) {
        System.out.println(message);

    }
}
