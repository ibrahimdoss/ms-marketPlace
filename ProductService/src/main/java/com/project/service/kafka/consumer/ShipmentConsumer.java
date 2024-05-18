package com.project.service.kafka.consumer;

import com.project.model.ProductEntity;
import com.project.service.ProductService;
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

    private final ProductService productService;

    public ShipmentConsumer(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "${spring.topic.stock.update_fail_shipment}", groupId = "shipmentFailGroupId")
    @RetryableTopic(
            dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR)
    public void listenStockUpdateFailShipment(String productId) {

        ProductEntity productById = productService.findProductById(Long.valueOf(productId));

        int numberOfProduct = productById.getNumberOfProduct();
        productById.setNumberOfProduct(++numberOfProduct);
        productService.saveProduct(productById);

    }

    //DLT yaparsam sadece prodcutun hatalarını okur. ayrı bir consuemr yaparsam ortak yerlerden okutabilrim.
    @DltHandler
    public void handleDltPayment(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String message) {
        //raporlama işlemi.

    }
}
