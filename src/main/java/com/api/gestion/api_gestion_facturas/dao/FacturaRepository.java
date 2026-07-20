package com.api.gestion.api_gestion_facturas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.gestion.api_gestion_facturas.pojo.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura,Integer>{
    List<Factura> getFacturas();

    List<Factura> getFacturasByUsername(@Param("username") String username);
}
