package com.api.gestion.api_gestion_facturas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.gestion.api_gestion_facturas.pojo.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura,Integer>{
    
}
