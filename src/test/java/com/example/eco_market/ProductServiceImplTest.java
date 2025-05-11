package com.example.eco_market;

import com.example.eco_market.Configurations.UserDetail;
import com.example.eco_market.DTO.JwtRequest;
import com.example.eco_market.DTO.JwtResponse;
import com.example.eco_market.Models.Product;
import com.example.eco_market.Models.User;
import com.example.eco_market.Repositories.ProductRepository;
import com.example.eco_market.Services.Impls.AuthServiceImpl;
import com.example.eco_market.Services.Impls.ProductServiceImpl;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testListProducts() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 10, "Russia", 1000, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, "Category 1", "image 1", null),
                new Product(2L, "Product 2", "Description 2", 10, "Russia", 1000, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, "Category 2", "image 2", null),
                new Product(3L, "Product 3", "Description 3", 10, "Russia", 1000, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, "Category 3", "image 3", null)
        );

        Mockito.when(productRepository.findByTitleContaining("Product")).thenReturn(products);

        List<Product> retrievedProducts = productService.listProducts("Product");

        Assert.assertEquals(products, retrievedProducts);

        Mockito.verify(productRepository, Mockito.times(1)).findByTitleContaining("Product");

        Mockito.when(productRepository.findAll()).thenReturn(products);

        retrievedProducts = productService.listProducts(null);

        Assert.assertEquals(products, retrievedProducts);

        Mockito.verify(productRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetProductById() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(new Product(1L, "Product 1", "Description 1", 10, "Russia", 1000, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, "Category 1", "image 1", null)));

        Product retrievedProduct = productService.getProductById(1L);

        Assert.assertNotNull(retrievedProduct);

        Assert.assertEquals(Optional.of(1L), Optional.ofNullable(retrievedProduct).map(Product::getId));

        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);

        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.empty());

        retrievedProduct = productService.getProductById(2L);

        Assert.assertNull(retrievedProduct);

        Mockito.verify(productRepository, Mockito.times(1)).findById(2L);
    }

    @Test
    public void testGetProductsByCategory() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 10, "Russia", 1000, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, "Category 1", "image 1", null),
                new Product(2L, "Product 2", "Description 2", 10, "Russia", 1000, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, "Category 2", "image 2", null),
                new Product(3L, "Product 3", "Description 3", 10, "Russia", 1000, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, "Category 3", "image 3", null)
        );

        Mockito.when(productRepository.findByCategory("Category 1")).thenReturn(Arrays.asList(products.get(0)));

        List<Product> retrievedProducts = productService.getProductsByCategory("Category 1");

        Assert.assertEquals(Arrays.asList(products.get(0)), retrievedProducts);

        Mockito.verify(productRepository, Mockito.times(1)).findByCategory("Category 1");
    }

    @Test
    public void testSaveProduct() {
        Product product = new Product(1L, "Product 1", "Description 1", 10, "Russia", 1000, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, "Category 1", "image 1", null);

        productService.saveProduct(product);

        Mockito.verify(productRepository, Mockito.times(1)).save(product);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product savedProduct = productService.getProductById(1L);
        System.out.println(savedProduct);

        Assert.assertNotNull(savedProduct);

        Assert.assertEquals(product, savedProduct);
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product(1L, "Product 1", "Description 1", 10, "Russia", 1000, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, "Category 1", "image 1", null);
        productService.saveProduct(product);
        Mockito.verify(productRepository, Mockito.times(1)).save(product);

        productService.deleteProduct(1L);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(1L);

        Product deletedProduct = productService.getProductById(1L);
        Assert.assertNull(deletedProduct);
    }
}