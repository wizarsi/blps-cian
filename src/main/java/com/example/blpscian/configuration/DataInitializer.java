package com.example.blpscian.configuration;

import com.example.blpscian.model.Role;
import com.example.blpscian.model.User;
import com.example.blpscian.model.enums.RoleName;
import com.example.blpscian.repositories.RoleRepository;
import com.example.blpscian.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role roleUser = new Role(1L, RoleName.USER);
        Role roleAdmin = new Role(2L, RoleName.ADMIN);
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
        User adminUser = new User("admin", "admin@admin.ru", passwordEncoder.encode("admin"), roleAdmin);
        userRepository.save(adminUser);
    }
}
