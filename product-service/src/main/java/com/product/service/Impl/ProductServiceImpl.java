package com.product.service.Impl;

import com.product.dao.ProductRepository;
import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.exception.ProductNotFoundException;
import com.product.model.Product;
import com.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("productService")
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest,Product.class);
        product.setCreatedAt(LocalDateTime.now());
        product.setModifiedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        log.info("Added product {} with Id {}",
                savedProduct.getName(),
                savedProduct.getId()
                );
        return modelMapper.map(savedProduct,ProductResponse.class);
    }

    @Override
    public ProductResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException("Product Not Found"));
        return modelMapper.map(product,ProductResponse.class);
    }

    @Override
    public List<ProductResponse> getProducts(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        return products.stream().map(product -> modelMapper.map(product,ProductResponse.class))
                .toList();
    }

    @Transactional
    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException("Product Not Found"));
        product.setModifiedAt(LocalDateTime.now());
        productRepository.delete(product);
        log.info("Deactivated product with Id {}",
                productId
                );
    }

    @Override
    public List<ProductResponse> getInactiveProduct() {
        java.util.List<Product> products = productRepository.getDeletedProduct();
        return products.stream().map(product -> modelMapper.map(product,ProductResponse.class))
                .toList();
    }

    @Override
    public void activateProduct(Long productId) {
        log.info("Starting to activate product with Id {}",
                productId
                );
        Product product = productRepository.inactiveProduct(productId)
                .orElseThrow(()->new ProductNotFoundException("Either product is deleted or you don't have permission"));
        log.info("Fetched softDeleted product {}",
                product.getName()
                );
        productRepository.restoreProductById(productId);
    }

    @Override
    public String patchQuantity(Long productId,Integer updateQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException("Product with given Id not found"));
        int updated = productRepository.decrementQuantity(productId,updateQuantity);
        if(updated>0) return"Product Quantity is updated having Id "+product.getId();
        return "No row updated";
    }
}
