package com.user.event.streaming.service.service.impl;

import com.user.event.streaming.service.dto.request.LoginRequest;
import com.user.event.streaming.service.dto.request.UserRegister;
import com.user.event.streaming.service.dto.response.ListResponse;
import com.user.event.streaming.service.dto.response.LoginResponse;
import com.user.event.streaming.service.dto.response.Response;
import com.user.event.streaming.service.dto.response.UserResponse;
import com.user.event.streaming.service.model.User;
import com.user.event.streaming.service.model.enums.ROLE;
import com.user.event.streaming.service.repository.UserRepository;
import com.user.event.streaming.service.security.JwtUtils;
import com.user.event.streaming.service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public Response registerUser(UserRegister userRegister) {
        if (userRepository.findByUsername(userRegister.getUsername()).isPresent()){
            return Response.builder()
                    .message("User Already Exists with username: " + userRegister.getUsername())
                    .success(false)
                    .object(null)
                    .build();
        }
        try {
            User user = new User();
            user.setName(userRegister.getName());
            user.setUsername(userRegister.getUsername());
            user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
            user.setRole(ROLE.USER);
            userRepository.save(user);
            UserResponse userResponse =  UserResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .build();

            return Response.builder()
                    .message("User Registered Successfully")
                    .success(true)
                    .object(userResponse)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.builder()
                    .message("Failed to register user: " + e.getMessage())
                    .success(false)
                    .object(null)
                    .build();
        }
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found after authentication"));

            String jwtToken = jwtUtils.generateToken(user.getUsername());

            LoginResponse loginResponse = LoginResponse.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .accessToken(jwtToken)
                    .refreshToken("sample-refresh-token")
                    .tokenType("Bearer")
                    .expiresIn(86400000L)
                    .roles(user.getRole().name())
                    .build();

            return Response.builder()
                    .message("Login Successful")
                    .success(true)
                    .object(loginResponse)
                    .build();

        } catch (AuthenticationException e) {
            return Response.builder()
                    .message("Invalid username or password")
                    .success(false)
                    .object(null)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .message("An error occurred: " + e.getMessage())
                    .success(false)
                    .object(null)
                    .build();
        }
    }

    @Override
    public Response findByUserId(Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                return Response.builder()
                        .message("User not found")
                        .success(false)
                        .object(null)
                        .build();
            }
            UserResponse userResponse = UserResponse.builder()
                    .id(user.get().getId())
                    .name(user.get().getName())
                    .username(user.get().getUsername())
                    .build();

            return Response.builder()
                    .message("User Fetched Successfully")
                    .success(true)
                    .object(userResponse)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.builder()
                    .message("Failed to fetch user: " + e.getMessage())
                    .success(false)
                    .object(null)
                    .build();
        }
    }

    @Override
    public Response updateUser(Long userId, User user){
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()){
                return Response.builder()
                        .message("User not found")
                        .success(false)
                        .object(null)
                        .build();
            }
            User user1 = userOpt.get();
            user1.setName(user.getName());
            user1.setUsername(user.getUsername());

            userRepository.save(user1);

            UserResponse userResponse = UserResponse.builder()
                    .id(user1.getId())
                    .name(user1.getName())
                    .username(user1.getUsername())
                    .build();

            return Response.builder()
                    .message("User Updated Successfully")
                    .success(true)
                    .object(userResponse)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.builder()
                    .message("Failed to update user: " + e.getMessage())
                    .success(false)
                    .object(null)
                    .build();
        }
    }

    @Override
    public ListResponse findAllUser() {
        try {
            List<User> users = userRepository.findAll();
            List<UserResponse> userResponses =  users.stream()
                    .map(user -> UserResponse.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .username(user.getUsername())
                            .build())
                    .toList();

            return ListResponse.builder()
                    .message("All User Fetched")
                    .success(true)
                    .object(Collections.singletonList(userResponses))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ListResponse.builder()
                    .message("Failed to fetch users: " + e.getMessage())
                    .success(false)
                    .object(Collections.emptyList())
                    .build();
        }
    }
}
