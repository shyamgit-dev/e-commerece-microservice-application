package com.user.service.Impl;

import com.user.dao.UserRepository;
import com.user.dto.UserRequest;
import com.user.dto.UserResponse;
import com.user.exception.InvalidActionException;
import com.user.exception.UserNotFoundException;
import com.user.model.User;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final Environment environment;

    @Override
    public UserResponse createUser(UserRequest request) {

        if(userRepository.existsByUsername(request.getUsername()))
            throw new InvalidActionException("Username Already Taken");

        if(userRepository.existsByEmail(request.getEmail()))
            throw new InvalidActionException("Email Already Registered");

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        log.info("User created with name {} and Id {}",
                savedUser.getName(),
                savedUser.getUserId()
                );

        return modelMapper.map(savedUser, UserResponse.class);
    }

    @Override
    public UserResponse getUser(Long userId) {
        log.info("Preparing to fetch userId");
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        log.info("Fetched User with Id {}",user.getUserId());
        log.info("Request handled by User Service on port {}",
                environment.getProperty("local.server.port"));
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public List<UserResponse> getUsers(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        return users.stream()
                .map(user -> modelMapper.map(user,UserResponse.class))
                .toList();
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        user.setModifiedAt(LocalDateTime.now());
        user.setName(user.getName()+"(deleted)");
        userRepository.delete(user);
    }
}
