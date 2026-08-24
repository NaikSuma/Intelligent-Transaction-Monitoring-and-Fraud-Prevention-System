package com.project.frauddetection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.frauddetection.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}