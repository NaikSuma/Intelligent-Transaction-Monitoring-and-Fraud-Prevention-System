package com.project.frauddetection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.frauddetection.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccount_AccountId(Long accountId);
    
    List<Transaction> findByIpAddress(String ipAddress);
    List<Transaction> findByDeviceId(String deviceId);
    
}