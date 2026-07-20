package com.api.gestion.api_gestion_facturas.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    ResponseEntity<Map<String, Object>> getCount();
}
