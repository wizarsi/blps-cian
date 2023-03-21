package com.example.blpscian.controllers;

import com.example.blpscian.exceptions.InvalidDataException;
import com.example.blpscian.exceptions.NoSuchUserException;
import com.example.blpscian.model.User;
import com.example.blpscian.model.dto.LoginRequestDTO;
import com.example.blpscian.model.dto.RegisterRequestDTO;
import com.example.blpscian.security.JwtUtil;
import com.example.blpscian.services.AuthorizationService;
import com.example.blpscian.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> authUser(@RequestBody LoginRequestDTO loginRequestDTO) throws NoSuchUserException {
        Map<Object, Object> model = new HashMap<>();
        User authUser = authorizationService.authUser(loginRequestDTO);
        model.put("token", jwtUtil.generateToken(authUser.getEmail()));
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) throws InvalidDataException {
        Map<Object, Object> model = new HashMap<>();
        User newUser = authorizationService.registerUser(registerRequestDTO);
        model.put("token", jwtUtil.generateToken(newUser.getEmail()));
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
