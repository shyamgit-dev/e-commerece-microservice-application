package com.user.controller;

import com.user.dto.UserRequest;
import com.user.dto.UserResponse;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request)
    {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long userId)
    {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(
           @RequestParam(required = false,name = "userIds") List<Long> userIds)
    {
        List<UserResponse> userResponses = userService.getUsers(userIds);
        return ResponseEntity.ok(userResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId)
    {
        String result = "Deleted User with Id "+userId;
        userService.deleteUser(userId);
        return ResponseEntity.ok(result);
    }
}
