package com.example.blpscian.security;

import com.example.blpscian.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.jaas.AuthorityGranter;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
public class CustomAuthorityGranter implements AuthorityGranter {
    private final UserRepository userRepository;
    @Override
    public Set<String> grant(Principal principal) {
        String role = String.valueOf(userRepository.findRoleByEmail(principal.getName()).getName());
        return Collections.singleton(role);
    }
}
