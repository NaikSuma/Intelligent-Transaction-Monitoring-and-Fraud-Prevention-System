package com.project.frauddetection.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txnId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "from_account")
    private Account fromAccount;
    
    

    @ManyToOne
    @JoinColumn(name = "to_account")
    private Account toAccount;


    @Positive
    private double amount;

    private LocalDateTime timestamp;

    @NotBlank
    private String ipAddress;
    
    @NotBlank
    private String deviceId;
}