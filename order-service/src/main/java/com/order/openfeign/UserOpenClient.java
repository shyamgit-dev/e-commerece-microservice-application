package com.order.openfeign;

import com.order.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",url = "http://localhost:8080/api/users")
public interface UserOpenClient {

    @GetMapping("/{id}")
    UserResponse fetchUserById(@PathVariable("id") Long userId);

}
