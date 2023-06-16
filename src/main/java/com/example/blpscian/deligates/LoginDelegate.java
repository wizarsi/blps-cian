package com.example.blpscian.deligates;

import com.example.blpscian.model.User;
import com.example.blpscian.model.dto.LoginRequestDto;
import com.example.blpscian.security.JwtUtil;
import com.example.blpscian.services.AuthorizationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class LoginDelegate implements JavaDelegate {
    private static final Logger logger = Logger.getLogger(LoginDelegate.class.getName());
    private final JwtUtil jwtUtil;
    private final AuthorizationService authService;

    public LoginDelegate(JwtUtil jwtUtil, AuthorizationService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            String username = (String) delegateExecution.getVariable("username");
            String password = (String) delegateExecution.getVariable("password");
            User user = authService.authUser(new LoginRequestDto(username, password));
            String token = jwtUtil.generateToken(user.getEmail());
            delegateExecution.setVariable("token", token);
            logger.log(Level.INFO, "Current activity is " + delegateExecution.getCurrentActivityName());
            logger.log(Level.INFO, "User with email " + username + " is successfully signed in");
            logger.log(Level.INFO, "Generated token is " + token);
        } catch (Throwable throwable) {
            throw new BpmnError("login_error", throwable.getMessage());
        }
    }
}
