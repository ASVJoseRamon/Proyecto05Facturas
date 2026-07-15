package com.api.gestion.api_gestion_facturas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.gestion.api_gestion_facturas.pojo.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Integer>{
    
    List<Categoria> getAllCategorias();
    
}
