package com.example.eco_market.Services;

import com.example.eco_market.Models.Order;
import com.example.eco_market.Models.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(Order order);
    List<Order> getAllUserOrders();
    List<Order> getAllOrders();
    void deleteOrder(Long id);
    Optional<Order> getOrderById(Long id);
}