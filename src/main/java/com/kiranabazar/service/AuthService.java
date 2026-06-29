package com.kiranabazar.service;

import com.kiranabazar.dto.AuthResponse;
import com.kiranabazar.dto.LoginRequest;
import com.kiranabazar.dto.RegistrationRequest;
import com.kiranabazar.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(RegistrationRequest request) {
        if (userService.usernameExists(request.getUsername())) {
            throw new IllegalArgumentException("Username already registered");
        }
        User user = new User(
                request.getName(),
                request.getAddress(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getCity(),
                request.getState(),
                request.getUsername(),
                request.getPassword());
        userService.registerUser(user);
    }

    public AuthResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String token = jwtService.generateToken(request.getUsername());
        return new AuthResponse(token, request.getUsername());
    }
}
