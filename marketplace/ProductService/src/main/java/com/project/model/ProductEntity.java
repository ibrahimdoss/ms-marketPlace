package com.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "products")
@Getter
@Setter
public class ProductEntity extends ProductBaseEntity implements Serializable {

    @Column
    private String name;

    @Column
    private String category;

    private String photoUrl;

    private String description;

    private Double price;

    private int numberOfProduct;

    private int weight;

    private static final long serialVersionUID = 1L;
}
