package com.example.blpscian.repositories;

import com.example.blpscian.model.Role;
import com.example.blpscian.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}
