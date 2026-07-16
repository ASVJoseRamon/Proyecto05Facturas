package com.api.gestion.api_gestion_facturas.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper;

public interface ProductoService {

    ResponseEntity<String> addNuevoProducto(Map<String, String> requestMap);
    
    ResponseEntity<List<ProductoWrapper>> getAllProductos();

    ResponseEntity<String> updateProducto(@RequestBody Map<String, String> requestMap);

    ResponseEntity<String> updateProductoFilter(@RequestBody Map<String,String> requestMap, Integer categoriaId, Integer id);
}
