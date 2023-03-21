package com.example.blpscian.repositories;

import com.example.blpscian.model.Role;
import com.example.blpscian.model.User;
import com.example.blpscian.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);

    User findUserByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findAllByRoleName(RoleName role);

    default Role findRoleByEmail(String email) {
        User user = findUserByEmail(email);
        return user.getRole();
    }
}
