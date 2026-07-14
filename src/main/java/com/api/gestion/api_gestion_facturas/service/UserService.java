package com.api.gestion.api_gestion_facturas.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.api.gestion.api_gestion_facturas.wrapper.UserWrapper;

public interface UserService {
    ResponseEntity<String> signUp(Map<String,String> requestMap);

    ResponseEntity<String> Login(Map<String, String> requesMap);

    ResponseEntity<List<UserWrapper>> getAllUsers();

    ResponseEntity<String> update(Map<String, String> requestMap);
}
