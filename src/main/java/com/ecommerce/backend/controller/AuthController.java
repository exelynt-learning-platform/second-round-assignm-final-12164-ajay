package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.*;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        service.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        String token = service.login(req.getUsername(), req.getPassword());
        return new AuthResponse(token);
    }
}