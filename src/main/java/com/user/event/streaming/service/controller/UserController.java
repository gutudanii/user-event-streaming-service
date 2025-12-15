package com.user.event.streaming.service.controller;

import com.user.event.streaming.service.dto.request.LoginRequest;
import com.user.event.streaming.service.dto.request.UserRegister;
import com.user.event.streaming.service.dto.response.ListResponse;
import com.user.event.streaming.service.dto.response.Response;
import com.user.event.streaming.service.model.User;
import com.user.event.streaming.service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> createResponse(@RequestBody UserRegister userRegister){
        return ResponseEntity.ok(userService.registerUser(userRegister));
    }

    @GetMapping("{userId}")
    public ResponseEntity<Response> getUserReponse(@PathVariable Long userId){
        return ResponseEntity.ok(userService.findByUserId(userId));
    }

    @PutMapping("{userId}")
    public ResponseEntity<Response> putUser(@PathVariable Long userId, @RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(userId, user));
    }

    @GetMapping("/all")
    public ResponseEntity<ListResponse> getAllUserResponse(){
        return ResponseEntity.ok(userService.findAllUser());
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(userService.login(request));
    }
}
