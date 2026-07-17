package com.api.gestion.api_gestion_facturas.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface FacturaService {
    ResponseEntity<String> generateReport(Map<String,Object> requestMap);
}
