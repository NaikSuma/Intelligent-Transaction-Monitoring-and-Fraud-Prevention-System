package com.project.frauddetection.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String name;

    private String email;

    private String phone;

    private String status; // ACTIVE / BLOCKED

    private Integer riskScore=0;
    
    private LocalDateTime createdAt;
}