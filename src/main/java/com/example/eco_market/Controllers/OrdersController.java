package com.example.eco_market.Controllers;

import com.example.eco_market.DTO.OrderRequest;
import com.example.eco_market.Models.Order;
import com.example.eco_market.Models.OrderProduct;
import com.example.eco_market.Models.Product;
import com.example.eco_market.Repositories.OrderProductRepository;
import com.example.eco_market.Repositories.OrderRepository;
import com.example.eco_market.Repositories.ProductRepository;
import com.example.eco_market.Repositories.UserRepository;
import com.example.eco_market.Services.Impls.OrderServiceImpl;
import com.example.eco_market.Services.OrderService;
import com.example.eco_market.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrdersController {

    private final OrderServiceImpl orderService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;

    @PostMapping
public ResponseEntity<Order> createOrder(@RequestBody OrderRequest requestBody) {
    Order order = new Order();
    List<OrderProduct> orderProducts = new ArrayList<>();

    Long userId = requestBody.getUserId();
    order.setUser(userRepository.findById(userId).orElse(null));
    order.setCost(requestBody.getCost());
    order.setDate(LocalDate.now());
    order.setAmount(requestBody.getAmount());
    order.setDeliveryAddress(requestBody.getDeliveryAddress());

    for (int i = 0; i < requestBody.getProductIds().size(); i++) {
        Long productId = requestBody.getProductIds().get(i);
        Integer quantity = requestBody.getQuantities().get(i);

        Product product = productRepository.findById(productId).orElse(null);

        if (product != null) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(quantity);
            orderProducts.add(orderProduct);
            product.setAmount(product.getAmount() - orderProduct.getQuantity());
        }
    }

    order.setOrderProducts(orderProducts);

    Order createdOrder = orderService.createOrder(order);
    orderProductRepository.saveAll(orderProducts);

    return ResponseEntity.ok(createdOrder);
}


    @GetMapping("/history")
    public List<Order> getAllUserOrders() {
        return orderService.getAllUserOrders();
    }

    @GetMapping("/history/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
}
