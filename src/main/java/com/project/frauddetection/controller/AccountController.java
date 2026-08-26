package com.project.frauddetection.controller; 

import java.time.LocalDateTime;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.project.frauddetection.model.Account;
import com.project.frauddetection.repository.AccountRepository;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    // TEST
    @GetMapping("/test")
    public String test() {
        return "Account API working!";
    }

    //  ADD ACCOUNT
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public Account addAccount(@RequestBody Account account) {
        account.setRiskScore(0); 
        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/unblock/{id}")
    public String unblock(@PathVariable Long id) {

        Account account = accountRepository.findById(id).orElseThrow();

        account.setStatus("ACTIVE");

        accountRepository.save(account);

        return "Account unblocked successfully";
    }
}
