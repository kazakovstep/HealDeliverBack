package com.example.eco_market.Controllers;

import com.example.eco_market.DTO.DeliveryPersonDTO;
import com.example.eco_market.DTO.UserDto;
import com.example.eco_market.Models.DeliveryPerson;
import com.example.eco_market.Models.Order;
import com.example.eco_market.Models.Product;
import com.example.eco_market.Models.User;
import com.example.eco_market.Repositories.DeliveryPersonRepository;
import com.example.eco_market.Services.DeliveryPersonService;
import com.example.eco_market.Services.Impls.OrderServiceImpl;
import com.example.eco_market.Services.Impls.ProductServiceImpl;
import com.example.eco_market.Services.Impls.UserServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;
    private final OrderServiceImpl orderService;

    private final DeliveryPersonService deliveryPersonService;
	private final DeliveryPersonRepository deliveryPersonRepository;

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

    @GetMapping("/delivery_person")
	public List<DeliveryPerson> getAllDeliveryPeople() {
		return deliveryPersonRepository.findAll();
	}
		

		@PostMapping("/delivery_person")
		public ResponseEntity<?> createDeliveryPerson(@RequestBody DeliveryPerson userForm) {
			if (deliveryPersonRepository.findById(userForm.getId()) != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь с такой почтой уже существует");
			}
				deliveryPersonService.createDeliveryPerson(userForm);
        return ResponseEntity.status(HttpStatus.CREATED).body("Доставщик успешно создан");
		}
		

    @PutMapping("/delivery_person")
    public ResponseEntity<?> updateDeliveryPerson(@RequestBody DeliveryPersonDTO userForm){
				deliveryPersonService.updateDeliveryPerson(userForm);
        return ResponseEntity.ok().build();
    }

		@DeleteMapping("/delivery_person")
    public ResponseEntity<?> deleteDeliveryPerson(@RequestBody DeliveryPersonDTO userForm){
				deliveryPersonService.deleteDeliveryPerson(userForm.getId());
        return ResponseEntity.ok().build();
    }
}
