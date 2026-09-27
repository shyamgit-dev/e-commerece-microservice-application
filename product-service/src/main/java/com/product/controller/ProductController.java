package com.product.controller;

import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest)
    {
        ProductResponse productResponse = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long productId)
    {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
           @RequestParam(required = false,name = "productIds") List<Long> productIds)
    {
        return ResponseEntity.ok(productService.getProducts(productIds));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId)
    {
        productService.deleteProduct(productId);
        String result = "Product Is Deactivated having Id "+productId;
        return ResponseEntity.ok(result);
    }

    @GetMapping("/in-active")
    public ResponseEntity<List<ProductResponse>> getInActiveProducts()
    {
        return ResponseEntity.ok(productService.getInactiveProduct());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> activateProduct(@PathVariable("id") Long productId)
    {
        productService.activateProduct(productId);
        String result = "Activated product having id "+productId;
        return ResponseEntity.ok(result);
    }
}
