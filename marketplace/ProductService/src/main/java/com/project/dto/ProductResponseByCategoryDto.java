package com.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductResponseByCategoryDto implements Serializable {

    private String name;

    private String category;

    private String description;

    private Double price;
}
