package com.example.eco_market.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<Long> productIds;
    private List<Integer> quantities;
    private Long userId;
    private int cost;
    private int amount;
    private String deliveryAddress;
}
