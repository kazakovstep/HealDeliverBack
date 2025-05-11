package com.example.eco_market;

import com.example.eco_market.DTO.UserDto;
import com.example.eco_market.Models.Role;
import com.example.eco_market.Models.User;
import com.example.eco_market.Repositories.UserRepository;
import com.example.eco_market.Services.Impls.UserServiceImpl;
import com.example.eco_market.Services.RoleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager em;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetCurrentUser() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User();
        user.setEmail("test@example.com");

        Mockito.when(authentication.getPrincipal()).thenReturn(user.getEmail());
        Mockito.when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);

        User currentUser = userService.getCurrentUser();

        Assert.assertEquals(user.getEmail(), currentUser.getEmail());
    }

    @Test
    public void testAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.allUsers();

        Assert.assertEquals(users.size(), allUsers.size());
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        Role role = new Role();
        role.setName("ROLE_USER");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(roleService.getUserRole()).thenReturn(role);
        Mockito.when(encoder.encode(user.getPassword())).thenReturn("encodedPassword");

        userService.saveUser(user);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);

        Assert.assertEquals(user.getRoles().get(0), role);
        Assert.assertEquals(user.getPassword(), "encodedPassword");
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        boolean deleted = userService.deleteUser(userId);

        Assert.assertTrue(deleted);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
    }

//    @Test
//    public void testUserGetList() {
//        Long idMin = 1L;
//        List<User> users = Arrays.asList(new User(), new User());
//
//        TypedQuery<User> query = Mockito.mock(TypedQuery.class);
//        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.eq(User.class))).thenReturn(query);
//        Mockito.when(query.setParameter("paramId", idMin)).thenReturn(query);
//        Mockito.when(query.getResultList()).thenReturn(users);
//
//        List<User> userList = userService.userGetList(idMin);
//
//        Assert.assertEquals(users.size(), userList.size());
//    }

//    @Test
//    public void testLoadUserByUsername() {
//        String email = "test@example.com";
//        User user = new User();
//        user.setEmail(email);
//
//        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(user);
//
//        UserDetails userDetails = userService.loadUserByUsername(email);
//
//        Assert.assertEquals(user.getEmail(), userDetails.getUsername());
//    }

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        User foundUser = userService.findByEmail(email);

        Assert.assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        Role role = new Role(1L, "ROLE_USER");
        UserDto updatedUser = new UserDto(userId, "newUsername", "newEmail@example.com", "newPassword", Collections.singletonList(role));

        User user = new User();
        user.setId(userId);

        Mockito.when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.of(user));
        Mockito.when(encoder.encode(updatedUser.getPassword())).thenReturn("encodedPassword");

        userService.updateUser(updatedUser);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Assert.assertEquals(updatedUser.getUsername(), user.getUsername());
        Assert.assertEquals(updatedUser.getEmail(), user.getEmail());
        Assert.assertEquals("encodedPassword", user.getPassword());
    }
}