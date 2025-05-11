package com.example.eco_market.Services.Impls;

import com.example.eco_market.Models.Order;
import com.example.eco_market.Models.User;
import com.example.eco_market.Repositories.OrderRepository;
import com.example.eco_market.Repositories.UserRepository;
import com.example.eco_market.Services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllUserOrders() {
        Long userId = userService.getCurrentUser().getId();
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<Order> getOrderById(Long id){
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
