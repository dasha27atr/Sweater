package com.example.sweater.repository;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);

    void deleteUserByRoles(Set<Role> roles);
}
