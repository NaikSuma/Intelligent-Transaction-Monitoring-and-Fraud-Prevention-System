package com.project.frauddetection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.frauddetection.model.Alert; 

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
