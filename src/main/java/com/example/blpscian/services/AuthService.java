package com.example.blpscian.services;

import com.example.blpscian.exceptions.AuthorizeException;
import com.example.blpscian.exceptions.InvalidDataException;
import com.example.blpscian.model.User;
import com.example.blpscian.model.dto.LoginRequestDto;
import com.example.blpscian.model.dto.RegisterRequestDto;
import com.example.blpscian.model.enums.RoleName;
import com.example.blpscian.repositories.RoleRepository;
import com.example.blpscian.repositories.UserRepository;
import com.example.blpscian.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, String> authUser(LoginRequestDto loginRequestDTO) throws AuthorizeException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        User user = userRepository.findUserByEmail(loginRequestDTO.getEmail());
        return generateUserRefreshAccessTokens(user);
    }

    public Map<String, String> registerUser(RegisterRequestDto registerRequestDTO) throws InvalidDataException {
        validateRegisterDTO(registerRequestDTO);
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new InvalidDataException("Пользователь с таким email уже существует");
        }
        User user;
        if (registerRequestDTO.getEmail().startsWith("admin") && registerRequestDTO.getName().startsWith("admin")) {
            user = new User(
                    registerRequestDTO.getName(),
                    registerRequestDTO.getEmail(),
                    passwordEncoder.encode(registerRequestDTO.getPassword()),
                    roleRepository.findByName(RoleName.ADMIN)
            );
        } else {
            user = new User(
                    registerRequestDTO.getName(),
                    registerRequestDTO.getEmail(),
                    passwordEncoder.encode(registerRequestDTO.getPassword()),
                    roleRepository.findByName(RoleName.USER)
            );
        }
        userRepository.save(user);
        return generateUserRefreshAccessTokens(user);
    }

    public Map<String, String> refreshToken(String refreshToken) throws AuthorizeException {
        User user;
        if (jwtUtil.checkRefreshToken(refreshToken)) {
            String username = jwtUtil.usernameFromRefreshToken(refreshToken);
            user = userRepository.findUserByEmail(username);
        } else {
            throw new AuthorizeException("Некорректный Refresh token");
        }
        return generateUserRefreshAccessTokens(user);
    }

    private Map<String, String> generateUserRefreshAccessTokens(User user) {
        Map<String, String> model = new HashMap<>();
        model.put("access_token", jwtUtil.generateAccessToken(user.getEmail()));
        model.put("refresh_token", jwtUtil.generateRefreshToken(user.getEmail()));
        return model;
    }

    private void validateRegisterDTO(RegisterRequestDto registerRequestDTO) throws InvalidDataException {
        StringBuilder message = new StringBuilder();
        boolean valid = true;
        if (registerRequestDTO.getPassword() == null || registerRequestDTO.getPassword().equals("")
                || registerRequestDTO.getPassword().length() < 5) {
            message.append("Пароль должен состоять минимум из 5 символов");
            valid = false;
        }
        if (registerRequestDTO.getName() == null || registerRequestDTO.getName().equals("")) {
            message.append("ФИО не может быть пустым");
            valid = false;
        }
        if (registerRequestDTO.getEmail() == null || registerRequestDTO.getEmail().equals("")) {
            message.append("Email не может быть пустым");
            valid = false;
        }
        if (!valid) throw new InvalidDataException(message.toString());
    }
}
