package com.example.eco_market.Controllers;
import com.example.eco_market.DTO.DeliveryPersonDTO;
import com.example.eco_market.DTO.OrderRequest;
import com.example.eco_market.Models.DeliveryPerson;
import com.example.eco_market.Models.Order;
import com.example.eco_market.Models.OrderProduct;
import com.example.eco_market.Models.Product;
import com.example.eco_market.Repositories.DeliveryPersonRepository;
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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery_person")
@CrossOrigin(origins = "http://185.182.219.60")
public class DeliveryPersonController {
    private final DeliveryPersonService deliveryPersonService;
		private final DeliveryPersonRepository deliveryPersonRepository;

		@GetMapping
		public List<DeliveryPerson> getAllDeliveryPeople() {
			return deliveryPersonRepository.findAll();
		}
		

		@PostMapping
		public ResponseEntity<?> createDeliveryPerson(@RequestBody DeliveryPerson userForm) {
			if (deliveryPersonRepository.findById(userForm.getId()) != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь с такой почтой уже существует");
			}
				deliveryPersonService.createDeliveryPerson(userForm);
        return ResponseEntity.status(HttpStatus.CREATED).body("Доставщик успешно создан");
		}
		

    @PutMapping
    public ResponseEntity<?> updateDeliveryPerson(@RequestBody DeliveryPersonDTO userForm){
				deliveryPersonService.updateDeliveryPerson(userForm);
        return ResponseEntity.ok().build();
    }

		@DeleteMapping
    public ResponseEntity<?> deleteDeliveryPerson(@RequestBody DeliveryPersonDTO userForm){
				deliveryPersonService.deleteDeliveryPerson(userForm.getId());
        return ResponseEntity.ok().build();
    }
}
