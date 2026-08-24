package com.project.frauddetection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.frauddetection.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}