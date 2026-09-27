package com.product.service;

import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse getProduct(Long productId);
    List<ProductResponse> getProducts(List<Long> productIds);
    void deleteProduct(Long productId);
    List<ProductResponse> getInactiveProduct();
    void activateProduct(Long productId);
}
