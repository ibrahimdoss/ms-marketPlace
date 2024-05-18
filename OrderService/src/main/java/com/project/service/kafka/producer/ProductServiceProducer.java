package com.project.service.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.kafka.CreateOrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProductServiceProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.topic.stock.update}")
    private String stockUpdateTopic;

    @Value("${spring.topic.stock-2.update.fail.shipment}")
    private String stockUpdateFailShipmentTopic;

    public ProductServiceProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessageKafkaStockUpdate(CreateOrderDto createOrderDto){
        String valueAsString = null;
        try {
            valueAsString = objectMapper.writeValueAsString(createOrderDto);
            CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(stockUpdateTopic, valueAsString);
            sendResultCompletableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + createOrderDto.orderNumber() +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            createOrderDto.orderNumber() + "] due to : " + ex.getMessage());
                }
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }catch (Exception e){
            log.error("Message is not sent ", e);
        }

    }
    public void sendMessageKafkaStockKontroFail(String productId){

        try {

            CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(stockUpdateFailShipmentTopic, productId);
            sendResultCompletableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + productId +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            productId + "] due to : " + ex.getMessage());
                }
            });

        }catch (Exception e){
            log.error("Message is not sent ", e);
        }

    }
}
