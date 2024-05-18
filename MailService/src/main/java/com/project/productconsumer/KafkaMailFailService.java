package com.project.productconsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMailFailService {


    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topics.mail-fail}")
    private String mailTopic;

    public KafkaMailFailService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessageKafka(String message){
        String valueAsString = null;
        try {

            CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(mailTopic, valueAsString);
            sendResultCompletableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + message +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            "] due to : " + ex.getMessage());
                }
            });

        }catch (Exception e){
            System.out.println("");
        }

    }
}
