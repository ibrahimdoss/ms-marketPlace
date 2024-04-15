package com.project.service.order;

import com.project.dto.ProductCountUpdateRequestDto;
import com.project.entity.OrderEntity;
import com.project.entity.OrderProductEntity;
import com.project.repository.OrderProductRepository;
import com.project.resttemplate.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final ProductClient productClient;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrderProduct(List<Long> productIdList, OrderEntity order) {
        productIdList
                .stream()
                .map(productClient::getProductInfoDto)
                .forEach(product -> {
                    OrderProductEntity orderProduct = new OrderProductEntity();
                    orderProduct.setOrder(order);
                    orderProduct.setProductId(product.id());
                    orderProductRepository.save(orderProduct);
                    int numberOfProduct = product.numberOfProduct();
                    ProductCountUpdateRequestDto updateRequestDto = ProductCountUpdateRequestDto.builder().id(product.id()).numberOfProduct(--numberOfProduct).build();
                    productClient.updateProductCount(updateRequestDto);
                    System.out.println(order);

                });
    }


    public List<OrderProductEntity> findAllByOrders(OrderEntity order){
        return orderProductRepository.findAllByOrder(order);
    }
}
