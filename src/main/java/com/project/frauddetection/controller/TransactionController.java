package com.project.frauddetection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.project.frauddetection.model.RiskResponse;
import com.project.frauddetection.model.Transaction;
import com.project.frauddetection.repository.TransactionRepository;
import com.project.frauddetection.service.TransactionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

//    // TEST
//    @GetMapping("/test")
//    public String test() {
//        return "Transaction API working!";
//    }

    //  ADD TRANSACTION
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public RiskResponse addTransaction(@Valid @RequestBody Transaction txn){
    	
        return transactionService.processTransaction(txn);
    }
    
    @GetMapping("/{accountId}")
    public List<Transaction> getTransactions(@PathVariable Long accountId) {
        return transactionRepository.findByFromAccount_AccountId(accountId);
    }
    
    
    
}