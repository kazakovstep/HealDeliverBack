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
import com.example.eco_market.Services.DeliveryZoneService;
import com.example.eco_market.Services.DeliveryPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://185.182.219.60")
public class OrdersController {

    private final OrderServiceImpl orderService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;
    private final DeliveryZoneService deliveryZoneService;
    private final DeliveryPersonService deliveryPersonService;

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

        // Преобразуем строковые координаты в Double
        Double latitude = null;
        Double longitude = null;
        try {
            if (requestBody.getDeliveryLatitude() != null) {
                latitude = Double.parseDouble(requestBody.getDeliveryLatitude().toString());
            }
            if (requestBody.getDeliveryLongitude() != null) {
                longitude = Double.parseDouble(requestBody.getDeliveryLongitude().toString());
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Неверный формат координат доставки");
        }

        order.setDeliveryLatitude(latitude);
        order.setDeliveryLongitude(longitude);

        // Назначаем зону доставки и доставщика
        if (latitude != null && longitude != null) {
            try {
                var deliveryZone = deliveryZoneService.findZoneForCoordinates(latitude, longitude);
                order.setDeliveryZone(deliveryZone);
                
                // Назначаем доставщика из этой зоны
                var deliveryPerson = deliveryPersonService.assignDeliveryPerson(deliveryZone.getId());
                if (deliveryPerson != null) {
                    order.setDeliveryPerson(deliveryPerson);
                }
            } catch (RuntimeException e) {
                throw new RuntimeException("Не удалось назначить зону доставки: " + e.getMessage());
            }
        }

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

    @GetMapping("/current")
    public Order getLastUserOrder(@RequestBody Long userId) {
        return orderService.getLastUserOrder(userId);
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
