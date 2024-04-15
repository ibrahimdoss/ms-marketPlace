package com.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    private List<Long> productIdList;

    private Long userId;

    private String orderDescription;
}
