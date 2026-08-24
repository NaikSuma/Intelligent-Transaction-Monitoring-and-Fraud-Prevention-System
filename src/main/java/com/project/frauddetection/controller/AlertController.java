package com.project.frauddetection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.project.frauddetection.repository.AlertRepository;
import com.project.frauddetection.model.Alert;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertRepository alertRepository;
    
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping
    public List<Alert> getAlerts() {
        return alertRepository.findAll();
    }
}