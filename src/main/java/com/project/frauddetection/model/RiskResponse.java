package com.project.frauddetection.model;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskResponse {

    private int risk;
    private List<String> reasons;
}