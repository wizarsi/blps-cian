package com.example.blpscian.security;

import com.example.blpscian.exceptions.NoSuchUserException;
import com.example.blpscian.model.User;
import com.example.blpscian.repositories.UserRepository;
import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Slf4j
public class JaasLoginModule implements LoginModule {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String username;
    private boolean loginSucceeded;
    private Subject subject;
    private CallbackHandler callbackHandler;
    private UserRepository userRepository;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.callbackHandler = callbackHandler;
        this.subject = subject;
        this.userRepository = (UserRepository) options.get("userRepository");
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback nameCallback = new NameCallback("username");
        PasswordCallback passwordCallback = new PasswordCallback("password", false);
        try {
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
            username = nameCallback.getName();
            String password = String.valueOf(passwordCallback.getPassword());
            User user = userRepository.findUserByEmail(username);
            if (user == null) {
                throw new NoSuchUserException("Пользователя с таким логином не существует");
            }
            loginSucceeded = passwordEncoder.matches(password, user.getPassword());
        } catch (NoSuchUserException e) {
            log.warn(e.getMessage());
            loginSucceeded = false;
        } catch (IOException | UnsupportedCallbackException e) {
            log.error("Error occurred during invocation of callback handler = {}", e.getMessage());
            loginSucceeded = false;
        }
        return loginSucceeded;
    }

    @Override
    public boolean commit() throws LoginException {
        if (!loginSucceeded) return false;
        if (username == null) throw new LoginException("Username is null");
        Principal principal = new UserPrincipal(username);
        subject.getPrincipals().add(principal);
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return false;
    }

    @Override
    public boolean logout() throws LoginException {
        return false;
    }
}
