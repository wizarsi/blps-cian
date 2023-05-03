package com.example.blpscian.controllers;

import com.example.blpscian.exceptions.InvalidDataException;
import com.example.blpscian.exceptions.NoSuchUserException;
import com.example.blpscian.model.User;
import com.example.blpscian.model.dto.LoginRequestDto;
import com.example.blpscian.model.dto.RegisterRequestDto;
import com.example.blpscian.security.JwtFilter;
import com.example.blpscian.security.JwtUtil;
import com.example.blpscian.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthorizationController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<?> authUser(@RequestBody LoginRequestDto loginRequestDTO) {
        return new ResponseEntity<>(authorizationService.authUser(loginRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequestDTO) throws InvalidDataException {
        return new ResponseEntity<>(authorizationService.registerUser(registerRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        String refreshToken = authorizationHeader.substring(7);
        return new ResponseEntity<>(authorizationService.refreshToken(refreshToken), HttpStatus.OK);
    }
}
