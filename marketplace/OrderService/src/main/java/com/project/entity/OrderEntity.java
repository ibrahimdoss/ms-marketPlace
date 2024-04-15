package com.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Table(name = "orders")
@Entity
@Getter
@Setter
public class OrderEntity extends OrderBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    private String orderNumber;
    private Date orderDate;
    private String orderDescription;
    private Double totalAmount;
    private Long userId;
}
