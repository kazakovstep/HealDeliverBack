package com.example.eco_market;

import com.example.eco_market.Models.Order;
import com.example.eco_market.Models.User;
import com.example.eco_market.Repositories.OrderRepository;
import com.example.eco_market.Services.Impls.OrderServiceImpl;
import com.example.eco_market.Services.Impls.UserServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testCreateOrder() {
        Order order = new Order();
        Mockito.when(orderRepository.save(order)).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);

        Mockito.verify(orderRepository, Mockito.times(1)).save(order);
        Assert.assertEquals(order, createdOrder);
    }


    @Test
    void testGetAllUserOrders() {
        Long userId = 1L;
        List<Order> userOrders = new ArrayList<>();

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        userOrders.add(order);

        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(orderRepository.findAllByUserId(userId)).thenReturn(userOrders);

        List<Order> retrievedOrders = orderService.getAllUserOrders();

        Mockito.verify(userService, Mockito.times(1)).getCurrentUser();
        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserId(userId);

        Assert.assertEquals(userOrders, retrievedOrders);
    }

    @Test
    void testGetOrderById() {
        Long orderId = 1L;
        Order order = new Order();
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<Order> retrievedOrder = orderService.getOrderById(orderId);

        Mockito.verify(orderRepository, Mockito.times(1)).findById(orderId);
        Assert.assertEquals(Optional.of(order), retrievedOrder);
    }

    @Test
    void testGetAllOrders() {
        List<Order> allOrders = new ArrayList<>();
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);

        List<Order> retrievedOrders = orderService.getAllOrders();

        Mockito.verify(orderRepository, Mockito.times(1)).findAll();
        Assert.assertEquals(allOrders, retrievedOrders);
    }

}