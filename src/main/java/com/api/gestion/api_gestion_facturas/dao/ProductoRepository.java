package com.api.gestion.api_gestion_facturas.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import com.api.gestion.api_gestion_facturas.pojo.Producto;
import com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper;

import jakarta.transaction.Transactional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Integer>{
        List<ProductoWrapper> getAllProductos();
        @Transactional
        @Modifying
        Producto updateProducto(@RequestBody Map<String, String> requestMap);
}
