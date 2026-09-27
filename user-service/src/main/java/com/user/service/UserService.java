package com.user.service;

import com.user.dto.UserRequest;
import com.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse getUser(Long userId);
    List<UserResponse> getUsers(List<Long> userIds);
    void deleteUser(Long userId);
}
