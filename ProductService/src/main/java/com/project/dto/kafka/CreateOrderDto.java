package com.project.dto.kafka;

public record CreateOrderDto(String orderDesc, String orderNumber, Long userId, Long productId) {
}
