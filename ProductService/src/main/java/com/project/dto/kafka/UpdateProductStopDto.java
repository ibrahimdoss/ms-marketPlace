package com.project.dto.kafka;

import java.io.Serializable;

public record UpdateProductStopDto(boolean successUpdateStock, String orderNumber) implements Serializable {
}
