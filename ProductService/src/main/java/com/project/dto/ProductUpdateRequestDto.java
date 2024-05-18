package com.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class ProductUpdateRequestDto {

    private long id;
    private String name;

    private String category;

    private String photoUrl;

    private String description;

    private BigDecimal price;

    private int numberOfProduct;
}
