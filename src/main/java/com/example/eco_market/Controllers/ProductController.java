package com.example.eco_market.Controllers;

import com.example.eco_market.Models.Product;
import com.example.eco_market.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public List<Product> products(@RequestParam(name = "title", required = false) String title) {
        return productService.listProducts(title);
    }

    @GetMapping("/product")
    public Product productById(@RequestParam(name = "id") Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/{category}")
    public List<Product> productsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<Resource> uploadImage(@PathVariable Long id) {
        try {
            String base64Image = productService.getProductById(id).getImage();
            String extension = base64Image.substring(base64Image.indexOf("/") + 1, base64Image.length() - 1);
            String fileName = UUID.randomUUID() + "." + extension;
            byte[] imageBytes = Base64.getDecoder().decode(base64Image.getBytes());
            File file = File.createTempFile(fileName, "." + extension);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(imageBytes);
            fos.close();
            Resource resource = new UrlResource(file.toURI());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
