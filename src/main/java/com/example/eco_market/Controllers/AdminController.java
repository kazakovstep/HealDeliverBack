package com.example.eco_market.Controllers;

import com.example.eco_market.DTO.UserDto;
import com.example.eco_market.Models.Order;
import com.example.eco_market.Models.Product;
import com.example.eco_market.Models.User;
import com.example.eco_market.Services.Impls.OrderServiceImpl;
import com.example.eco_market.Services.Impls.ProductServiceImpl;
import com.example.eco_market.Services.Impls.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;
    private final OrderServiceImpl orderService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.allUsers();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody UserDto user) {
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/product/create")
    public String createProduct(Product product) {
        productService.saveProduct(product);
        return "redirect:/";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
