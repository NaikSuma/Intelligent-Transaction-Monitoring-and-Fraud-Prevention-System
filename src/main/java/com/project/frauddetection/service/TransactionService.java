package com.project.frauddetection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.frauddetection.model.Transaction;
import com.project.frauddetection.model.Account;
import com.project.frauddetection.model.RiskResponse;
import com.project.frauddetection.model.Alert;
import com.project.frauddetection.repository.TransactionRepository;
import com.project.frauddetection.repository.AccountRepository;
import com.project.frauddetection.repository.AlertRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionService.class);

    @Value("${fraud.threshold}")
    private int threshold;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private FraudDetectionService fraudDetectionService;

    public RiskResponse processTransaction(Transaction txn) {

        logger.info("Processing transaction from account {}",
                txn.getFromAccount().getAccountId());

        // Set timestamp
        txn.setTimestamp(LocalDateTime.now());

        // Save transaction
        transactionRepository.save(txn);
        logger.info("Transaction saved successfully");

        // Get account ID
        Long accountId = txn.getFromAccount().getAccountId();

        // Fetch transactions
        List<Transaction> transactions =
                transactionRepository.findByFromAccount_AccountId(accountId);

        // Fetch account
        Account account = accountRepository.findById(accountId).orElse(null);

        if (account == null) {
            logger.error("Account not found for ID {}", accountId);
            throw new RuntimeException("Account not found");
        }

        //int accountAgeDays = 10;
        int accountAgeDays = (int) java.time.temporal.ChronoUnit.DAYS.between(
                account.getCreatedAt(),
                LocalDateTime.now()
        );

        // Base risk calculation
        RiskResponse response =
                fraudDetectionService.calculateRiskWithReasons(transactions, accountAgeDays);

        int risk = response.getRisk();
        List<String> reasons = response.getReasons();

        logger.info("Initial risk calculated: {}", risk);
        
     // High transaction amount detection
        if (txn.getAmount() > 50000) {
            risk += 30;
            reasons.add("High transaction amount");
        }
        
     // Compare with customer's historical average transaction amount
        double averageAmount = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .average()
                .orElse(0);

        if (averageAmount > 0 && txn.getAmount() > averageAmount * 3) {
            risk += 25;
            reasons.add("Transaction amount significantly higher than average");
        }

        // IP-based detection
        List<Transaction> sameIpTransactions =
                transactionRepository.findByIpAddress(txn.getIpAddress());
        
     // New IP detection
        boolean newIp = transactions.stream()
                .filter(t -> t.getIpAddress() != null)
                .noneMatch(t -> t.getIpAddress().equals(txn.getIpAddress()));

        if (newIp) {
            risk += 15;
            reasons.add("New IP address detected");
        }

        if (sameIpTransactions.size() > 5) {
            risk += 40;
            reasons.add("Same IP used across accounts");
        }

        //  Device-based detection
        List<Transaction> sameDeviceTransactions =
                transactionRepository.findByDeviceId(txn.getDeviceId());
        
     // New device detection
        boolean newDevice = transactions.stream()
        	    .filter(t -> t.getDeviceId() != null)
        	    .noneMatch(t -> t.getDeviceId().equals(txn.getDeviceId()));

        if (newDevice) {
            risk += 15;
            reasons.add("New device detected");
        }

        if (sameDeviceTransactions.size() > 5) {
            risk += 40;
            reasons.add("Same device used across accounts");
        }

        logger.info("Final risk after checks: {}", risk);
        logger.info("Reasons: {}", reasons);

        // Update account
        account.setRiskScore(risk);

        //  Alert creation
        if (risk > threshold) {

            account.setStatus("BLOCKED");

            Alert alert = new Alert();
            alert.setAccountId(account.getAccountId());
            alert.setRiskScore(risk);
            alert.setReason(String.join(", ", reasons));
            alert.setTimestamp(LocalDateTime.now());

            alertRepository.save(alert);

            logger.warn("Account {} BLOCKED due to high risk {}",
                    account.getAccountId(), risk);
        }

        // Save account
        accountRepository.save(account);

        logger.info("Transaction processing completed for account {}", accountId);

        // Return response
        return new RiskResponse(risk, reasons);
    }
}