package com.api.gestion.api_gestion_facturas.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.api.gestion.api_gestion_facturas.pojo.Factura;

public interface FacturaService {
    ResponseEntity<String> generateReport(Map<String,Object> requestMap);

    ResponseEntity<List<Factura>> getFacturas(); 

    ResponseEntity<byte[]> getPDF(Map<String, Object> requestMap);

    ResponseEntity<String> deleteFactura(Integer id);

}
