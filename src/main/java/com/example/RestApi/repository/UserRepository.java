package com.example.RestApi.repository;

import com.example.RestApi.model.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Users findByEmail(String email);

    boolean existsById(Long id);

}
