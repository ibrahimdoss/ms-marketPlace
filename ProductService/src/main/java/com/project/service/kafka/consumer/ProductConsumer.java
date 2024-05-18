package com.project.service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.kafka.CreateOrderDto;
import com.project.model.ProductEntity;
import com.project.service.ProductService;
import com.project.service.kafka.producer.OrderProducer;
import com.project.service.kafka.producer.ShipmentProducer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;



@Component
public class ProductConsumer {


    private final ObjectMapper objectMapper;
    private final ProductService productService;

    private final ShipmentProducer kafkaShipmentService;

    private final OrderProducer kafkaOrderService;

    public ProductConsumer(ObjectMapper objectMapper, ProductService productService, ShipmentProducer kafkaShipmentService, OrderProducer kafkaOrderService) {
        this.objectMapper = objectMapper;
        this.productService = productService;
        this.kafkaShipmentService = kafkaShipmentService;
        this.kafkaOrderService = kafkaOrderService;
    }

    @KafkaListener(topics = "${spring.topic.stock.update}", groupId = "myGroups")
    @RetryableTopic( attempts = "1",
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    public void listen(String message) {
        CreateOrderDto createOrderDto = null;
        try {

            createOrderDto = objectMapper.readValue(message, CreateOrderDto.class);

            Long productId = createOrderDto.productId();
            ProductEntity productById = productService.findProductById(productId);

            int numberOfProduct = productById.getNumberOfProduct();
            productById.setNumberOfProduct(--numberOfProduct);
            ProductEntity product = productService.saveProduct(productById);
            String s = null;
            s.equals("");
            kafkaShipmentService.sendMessageKafkaShipment(createOrderDto);


            //      kafkaProductService.sendMessageKafkaStockKontrol(true, createOrderDto.orderNumber());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        catch (Exception e){
//            Product productById = productService.findProductById(createOrderDto.productId());
//            int numberOfProduct = productById.getNumberOfProduct();
//            productById.setNumberOfProduct(++numberOfProduct);
//            productService.saveProduct(productById);
//            kafkaOrderService.sendMessageKafkaStockKontroFail(createOrderDto.orderNumber());
//
//        }
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

    @KafkaListener(topics = "${spring.topic.stock.update_fail_shipment}", groupId = "shipmentFailGroupId")
    @RetryableTopic( attempts = "1",
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    public void listenStockUpdateFailShipment2(String productId) {

        ProductEntity productById = productService.findProductById(Long.valueOf(productId));

        int numberOfProduct = productById.getNumberOfProduct();
        productById.setNumberOfProduct(++numberOfProduct);
        productService.saveProduct(productById);


    }
}
