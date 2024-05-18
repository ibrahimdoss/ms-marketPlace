package com.project.service.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.kafka.MailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class MailServiceProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.topic.mail}")
    private String mailTopic;

    public MailServiceProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessageKafka(MailDto mailDto){
        String valueAsString = null;
        try {
            valueAsString = objectMapper.writeValueAsString(mailDto);
            CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(mailTopic, valueAsString);
            sendResultCompletableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + mailDto.mailBody() +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            mailDto.mailBody() + "] due to : " + ex.getMessage());
                }
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }catch (Exception e){
            log.error("Message is not sent ", e);
        }

    }
}
