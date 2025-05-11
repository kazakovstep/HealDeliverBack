package com.example.eco_market;

import com.example.eco_market.Configurations.UserDetail;
import com.example.eco_market.DTO.JwtRequest;
import com.example.eco_market.DTO.JwtResponse;
import com.example.eco_market.Models.User;
import com.example.eco_market.Services.Impls.AuthServiceImpl;
import com.example.eco_market.Services.Impls.UserServiceImpl;
import com.example.eco_market.utils.JwtTokenUtils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationTokenCaptor;

    @Test
    public void testCreateAuthToken_WithValidCredentials_ReturnsToken() {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        UserDetail userDetails = new UserDetail(user);

        String token = "testToken";

        Mockito.when(userService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        Mockito.when(jwtTokenUtils.generateToken(userDetails)).thenReturn(token);

        Mockito.doAnswer(invocation -> null).when(authenticationManager)
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<?> response = authService.createAuthToken(authRequest);

        Mockito.verify(authenticationManager).authenticate(authenticationTokenCaptor.capture());
        UsernamePasswordAuthenticationToken authenticationToken = authenticationTokenCaptor.getValue();

        Assert.assertEquals(authRequest.getEmail(), authenticationToken.getPrincipal());
        Assert.assertEquals(authRequest.getPassword(), authenticationToken.getCredentials());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(new JwtResponse(token), response.getBody());
    }

    @Test
    public void testCreateAuthToken_WithInvalidCredentials_ReturnsUnauthorized() {
        // Arrange
        JwtRequest authRequest = new JwtRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("wrongPassword");

        Mockito.doThrow(BadCredentialsException.class).when(authenticationManager)
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<?> response = authService.createAuthToken(authRequest);

        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}