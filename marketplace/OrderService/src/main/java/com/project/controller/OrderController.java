package com.project.controller;

import com.project.dto.OrderRequestDto;
import com.project.service.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/save")
    public void save(@RequestBody OrderRequestDto orderRequestDto){
        orderService.save(orderRequestDto);
    }

    @DeleteMapping("/deleteOrderByOrderId")
    public void deleteOrderByOrderNumber(@RequestParam Long orderID){
        orderService.deleteOrderByOrderNumberCascade(orderID);
    }


    @PostMapping("/retunOorderByOrderId")
    public ResponseEntity<String> retunOorderByOrderId(@RequestParam Long orderID, @RequestParam Long userId){
        String orderReturnMessage = orderService.retunOorderByOrderId(orderID, userId);
        return ResponseEntity.ok().body(orderReturnMessage);
    }
}
