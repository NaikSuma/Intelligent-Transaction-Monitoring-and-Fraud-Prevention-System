package com.project.frauddetection.service;

import org.springframework.stereotype.Service;

import com.project.frauddetection.model.RiskResponse;
import com.project.frauddetection.model.Transaction;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FraudDetectionService {

    // Rule Constants
    private static final int HIGH_FREQUENCY_LIMIT = 10;
    private static final int RAPID_TRANSACTION_MINUTES = 5;
    private static final int NEW_ACCOUNT_DAYS = 30;

    private static final int HIGH_FREQUENCY_RISK = 25;
    private static final int RAPID_TRANSACTION_RISK = 30;
    private static final int NEW_ACCOUNT_RISK = 20;

    public RiskResponse calculateRiskWithReasons(List<Transaction> transactions,
                                                 int accountAgeDays) {

        int risk = 0;
        List<String> reasons = new ArrayList<>();

        // Sort latest transactions first
        transactions.sort(
                Comparator.comparing(Transaction::getTimestamp).reversed()
        );

        // Rule 1: High transaction frequency
        if (transactions.size() > HIGH_FREQUENCY_LIMIT) {
            risk += HIGH_FREQUENCY_RISK;
            reasons.add("High transaction frequency");
        }

        // Rule 2: Rapid consecutive transactions
        if (transactions.size() > 1) {

            long minutes = Duration.between(
                    transactions.get(1).getTimestamp(),
                    transactions.get(0).getTimestamp()
            ).toMinutes();

            if (minutes < RAPID_TRANSACTION_MINUTES) {
                risk += RAPID_TRANSACTION_RISK;
                reasons.add("Rapid transactions");
            }
        }

        // Rule 3: New account
        if (accountAgeDays < NEW_ACCOUNT_DAYS) {
            risk += NEW_ACCOUNT_RISK;
            reasons.add("New account");
        }

        return new RiskResponse(risk, reasons);
    }
}