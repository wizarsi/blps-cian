package com.example.blpscian.deligates;

import com.example.blpscian.model.dto.LoginRequestDto;
import com.example.blpscian.security.JwtUtil;
import com.example.blpscian.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.Map;

@Component
@Named
@Slf4j
public class LoginDelegate implements JavaDelegate {
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public LoginDelegate(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            String username = (String) delegateExecution.getVariable("email");
            String password = (String) delegateExecution.getVariable("password");
            Map<String, String> map = authService.authUser(new LoginRequestDto(username, password));
            String token = map.get("access_token");
            delegateExecution.setVariable("token", token);
            log.info("Current activity is " + delegateExecution.getCurrentActivityName());
            log.info("User with email " + username + " is successfully signed in");
            log.info("Generated token is " + token);
        } catch (Throwable throwable) {
            throw new BpmnError("login_error", throwable.getMessage());
        }
    }
}
