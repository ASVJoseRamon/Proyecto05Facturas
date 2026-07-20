package com.api.gestion.api_gestion_facturas.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.gestion.api_gestion_facturas.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    
    private final DashboardService dashboardService;

    @GetMapping("/detalles")
    public ResponseEntity<Map<String, Object>> getCount(){
        return dashboardService.getCount();
    }
}
