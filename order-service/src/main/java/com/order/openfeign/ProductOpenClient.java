package com.order.openfeign;

import com.order.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="product-service")//,url = "http://localhost:8081/api/products")
public interface ProductOpenClient {

    @GetMapping("/api/products")
    List<ProductResponse> fetchProductByIds(
            @RequestParam(name = "productIds", required = false) List<Long> productIds);

    @PatchMapping("/api/products/{id}/{stock}")
    String patchQuantity(@PathVariable("id") Long productId,
                         @PathVariable("stock") Integer quantity);
}
