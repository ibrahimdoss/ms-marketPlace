package com.project.service.kafka.consumer;

import com.project.dto.ProductUpdateRequestDto;
import com.project.dto.kafka.CreateOrderDto;
import com.project.model.ProductEntity;
import com.project.service.ProductService;
import com.project.service.kafka.producer.OrderProducer;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@Component
public class ProductUpdateConsumer {


    private final ObjectMapper objectMapper;
    private final ProductService productService;

    private final OrderProducer kafkaOrderService;

    private final ModelMapper modelMapper;

    public ProductUpdateConsumer(ObjectMapper objectMapper, ProductService productService, OrderProducer kafkaOrderService, ModelMapper modelMapper) {
        this.objectMapper = objectMapper;
        this.productService = productService;
        this.kafkaOrderService = kafkaOrderService;
        this.modelMapper = modelMapper;
    }

    @KafkaListener(topics = "${spring.topic.update.product}", groupId = "productUpdateGroupId")
    @RetryableTopic( attempts = "1",
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    public void listen(String message) {
        ProductUpdateRequestDto productUpdateRequestDto = null;
        try {

            productUpdateRequestDto = objectMapper.readValue(message, ProductUpdateRequestDto.class);

            long id = productUpdateRequestDto.getId();
            ProductEntity product = productService.findProductById(id);
            product.setCategory(productUpdateRequestDto.getCategory());
            product.setName(productUpdateRequestDto.getName());
            productService.saveProduct(product);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    //DLT yaparsam sadece prodcutun hatalarını okur. ayrı bir consuemr yaparsam ortak yerlerden okutabilrim.
    @DltHandler
    public void handleDltPayment(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String message) {
        CreateOrderDto createOrderDto = null;
        try {
            createOrderDto = objectMapper.readValue(message, CreateOrderDto.class);
            ProductEntity productById = productService.findProductById(createOrderDto.productId());
            int numberOfProduct = productById.getNumberOfProduct();
            productById.setNumberOfProduct(++numberOfProduct);
            productService.saveProduct(productById);
            kafkaOrderService.sendMessageKafkaStockControlFail(createOrderDto.orderNumber());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @KafkaListener(topics = "${spring.topic.stock.update_fail_shipment}", groupId = "shipmentFailGroupId")
    @RetryableTopic( attempts = "1",
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    public void listenStockUpdateFailShipment(String productId) {

        ProductEntity productById = productService.findProductById(Long.valueOf(productId));

        int numberOfProduct = productById.getNumberOfProduct();
        productById.setNumberOfProduct(++numberOfProduct);
        productService.saveProduct(productById);


    }
}
