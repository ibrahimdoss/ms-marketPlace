package com.project.repository;

import com.project.entity.OrderEntity;
import com.project.entity.OrderProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderProductRepository extends CrudRepository<OrderProductEntity, Long> {

    @Query("SELECT po FROM OrderProductEntity po WHERE po.order.id = :orderId")
    List<OrderProductEntity> findProductOrdersByOrderId(Long orderId);

    List<OrderProductEntity> findAllByOrder(OrderEntity order);
}
