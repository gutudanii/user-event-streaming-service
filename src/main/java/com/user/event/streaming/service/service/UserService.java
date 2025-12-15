package com.user.event.streaming.service.service;

import com.user.event.streaming.service.dto.request.LoginRequest;
import com.user.event.streaming.service.dto.request.UserRegister;
import com.user.event.streaming.service.dto.response.ListResponse;
import com.user.event.streaming.service.dto.response.Response;
import com.user.event.streaming.service.model.User;

import java.util.UUID;

public interface UserService {
    Response registerUser(UserRegister userRegister);

    Response login(LoginRequest loginRequest);

    Response findByUserId(Long userId);

    Response updateUser(Long userId, User user);

    ListResponse findAllUser();
}
