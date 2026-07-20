package com.api.gestion.api_gestion_facturas.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.gestion.api_gestion_facturas.dao.CategoriaRepository;
import com.api.gestion.api_gestion_facturas.dao.FacturaRepository;
import com.api.gestion.api_gestion_facturas.dao.ProductoRepository;
import com.api.gestion.api_gestion_facturas.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{

    private final ProductoRepository productoDAO;
    
    private final CategoriaRepository categoriaDAO;

    private final FacturaRepository facturaDAO;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map = new HashMap<>();
        map.put("productos",productoDAO.count());
        map.put("categorias",categoriaDAO.count());
        map.put("facturas",facturaDAO.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
}
