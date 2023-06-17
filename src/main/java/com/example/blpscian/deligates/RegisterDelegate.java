package com.example.blpscian.deligates;

import com.example.blpscian.model.dto.RegisterRequestDto;
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
public class RegisterDelegate implements JavaDelegate {
    private final AuthService authService;

    public RegisterDelegate(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            String name = (String) delegateExecution.getVariable("name");
            String username = (String) delegateExecution.getVariable("email");
            String password = (String) delegateExecution.getVariable("password");
            Map<String, String> map = authService.registerUser(new RegisterRequestDto(name, username, password));
            log.info("Current activity is " + delegateExecution.getCurrentActivityName());
            //log.info("New user signed up with username=" + user.getEmail() + " and id=" + user.getId());
        } catch (Throwable throwable) {
            throw new BpmnError("register_error", throwable.getMessage());
        }
    }
}
