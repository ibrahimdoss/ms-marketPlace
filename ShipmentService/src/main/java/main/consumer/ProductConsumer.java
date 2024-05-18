package main.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.dto.ShipmentCargoDto;
import main.producer.OrderProducer;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ProductConsumer {

    private final OrderProducer orderProducer;
    private final ObjectMapper objectMapper;

    public ProductConsumer(OrderProducer orderProducer, ObjectMapper objectMapper) {
        this.orderProducer = orderProducer;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "${topic.shipment.create}", groupId = "productShipmentGroupId")
    @RetryableTopic( attempts = "1",
            dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR)
    public void listen(String message) {
        ShipmentCargoDto shipmentCargoDto = null;
        try {
            shipmentCargoDto = objectMapper.readValue(message, ShipmentCargoDto.class);
            //createOrder
            //persistOrder
            System.out.println(shipmentCargoDto);

            orderProducer.sendMessageKafkaUpdateOrderSuccess(shipmentCargoDto.orderNumber());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    @DltHandler
    public void handleDltPayment(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String message) {
        ShipmentCargoDto shipmentCargoDto = null;

        try {
            shipmentCargoDto = objectMapper.readValue(message, ShipmentCargoDto.class);
            //createOrder
            //persistOrder
            System.out.println(shipmentCargoDto);

            orderProducer.sendMessageKafkaShipmentFail(shipmentCargoDto);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
