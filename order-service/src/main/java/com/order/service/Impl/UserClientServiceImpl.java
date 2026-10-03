package com.order.service.Impl;

import com.order.dto.UserResponse;
import com.order.openfeign.UserOpenClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserClientServiceImpl {

    private final UserOpenClient userOpenClient;

    @CircuitBreaker(name = "userService",fallbackMethod = "userServiceFallback")
    public UserResponse getUserById(Long userId)
    {
        return userOpenClient.fetchUserById(userId);
    }

    public UserResponse userServiceFallback(Long userId,Throwable throwable)
    {
        log.warn("User service is temporarily Unavailable please try again later {}",
                throwable.getMessage()
        );
        return null;
    }
}
