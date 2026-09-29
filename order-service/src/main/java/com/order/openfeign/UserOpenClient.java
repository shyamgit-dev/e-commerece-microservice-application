package com.order.openfeign;

import com.order.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//Eureka-Server Knows localhost:8080=user-service
@FeignClient(name = "user-service")//url = "http://localhost:8080/api/users")
public interface UserOpenClient {


    //Feign knows /api/users/{id} using interfaces
    @GetMapping("/api/users/{id}")
    UserResponse fetchUserById(@PathVariable("id") Long userId);

    //so EUREKA-SERVER + FEIGN CLIENT = http://localhost:8080/api/users/{id}

}
