package com.example.eco_market.Services;

import com.example.eco_market.DTO.UserDto;
import com.example.eco_market.Models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    User getCurrentUser();
    List<User> allUsers();
    void saveUser(User user);
    boolean deleteUser(Long userId);
    List<User> userGetList(Long idMin);
    User findByEmail(String email);

    void updateUser(UserDto updatedUser);
}
