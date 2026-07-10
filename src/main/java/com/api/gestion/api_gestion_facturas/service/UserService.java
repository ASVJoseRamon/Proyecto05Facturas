package com.api.gestion.api_gestion_facturas.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> signUp(Map<String,String> requestMap);
}
