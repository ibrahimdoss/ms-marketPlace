package main.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.dto.ShipmentCargoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderProducer {


    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    @Value("${topic.update.order.success}")
    private String updateOrderSuccess;

    @Value("${topic.create.shipment.fail}")
    private String createShipmentFail;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessageKafkaUpdateOrderSuccess(String orderNumber){

        try {

            CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(updateOrderSuccess, orderNumber);
            sendResultCompletableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + orderNumber +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            orderNumber + "] due to : " + ex.getMessage());
                }
            });

        }catch (Exception e){
            System.out.println("hata");
        }

    }

    public void sendMessageKafkaShipmentFail(ShipmentCargoDto shipmentCargoDto){

        try {
            String shipmentCargoDtoStr = objectMapper.writeValueAsString(shipmentCargoDto);

            CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(createShipmentFail, shipmentCargoDtoStr);
            sendResultCompletableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + shipmentCargoDto +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            shipmentCargoDto + "] due to : " + ex.getMessage());
                }
            });

        }catch (Exception e){
            System.out.println("hata");
        }

    }
}
