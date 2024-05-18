package com.project.service.kafka.consumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.kafka.ShipmentCargoDto;
import com.project.entity.OrderEntity;
import com.project.enums.OrderState;
import com.project.service.kafka.producer.ProductServiceProducer;
import com.project.service.order.OrderService;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ShipmentConsumer {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    private final ProductServiceProducer productServiceProducer;

    public ShipmentConsumer(OrderService orderService, ObjectMapper objectMapper, ProductServiceProducer productServiceProducer) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
        this.productServiceProducer = productServiceProducer;
    }

    @KafkaListener(topics = "${spring.topic.update.order.success}", groupId = "shipmentOrderGroupId")
    @RetryableTopic( attempts = "1",
            dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR)
    public void listen(String orderNumber) {
        OrderEntity order = orderService.findOrderByOrderNumber(orderNumber);
        order.setOrderState(OrderState.CREATED);
        orderService.saveOrder(order);
    }

    @DltHandler
    public void handleDltPayment(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String orderNumber) {
        OrderEntity order = orderService.findOrderByOrderNumber(orderNumber);
        order.setOrderState(OrderState.CANCELED);
        orderService.saveOrder(order);

    }


    @KafkaListener(topics = "${spring.topic.create.shipment.fail}", groupId = "shipmentOrderGroupId")
    @RetryableTopic(
            dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR)
    public void listenFailCreateShipment(String message) {
        try {
            ShipmentCargoDto shipmentCargoDto = objectMapper.readValue(message, ShipmentCargoDto.class);
            OrderEntity order = orderService.findOrderByOrderNumber(shipmentCargoDto.orderNumber());
            order.setOrderState(OrderState.CANCELED);
            orderService.saveOrder(order);
            productServiceProducer.sendMessageKafkaStockKontroFail(order.getOrderNumber());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    @DltHandler
    public void handleDltFailShipment(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String orderNumber) {
        OrderEntity order = orderService.findOrderByOrderNumber(orderNumber);
        order.setOrderState(OrderState.CANCELED);
        orderService.saveOrder(order);

    }
}
