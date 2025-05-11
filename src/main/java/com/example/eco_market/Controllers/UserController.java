package com.example.eco_market.Controllers;

import com.example.eco_market.DTO.JwtRequest;
import com.example.eco_market.DTO.RegistrationUserDto;
import com.example.eco_market.DTO.UserDto;
import com.example.eco_market.Models.User;
import com.example.eco_market.Services.Impls.AuthServiceImpl;
import com.example.eco_market.Services.Impls.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User userForm) {
        if (userService.findByEmail(userForm.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь с такой почтой уже существует");
        }
        userService.saveUser(userForm);
        return ResponseEntity.ok(new RegistrationUserDto(userForm.getEmail(), userForm.getPassword()));
    }

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @GetMapping("/user")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userForm){
        userService.updateUser(userForm);
        return ResponseEntity.ok().build();
    }
}
