package com.api.gestion.api_gestion_facturas.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.api.gestion.api_gestion_facturas.pojo.Categoria;

public interface CategoriaService {

    ResponseEntity<String> addNuevaCategoria(Map<String,String> requestMap);    

    ResponseEntity<List<Categoria>> getAllCategorias(String valueFilter);

    ResponseEntity<String>  updateCategoria(Map<String,String> requestMap);
}
