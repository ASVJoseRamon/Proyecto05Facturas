package com.api.gestion.api_gestion_facturas.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.gestion.api_gestion_facturas.constantes.FacturaConstantes;
import com.api.gestion.api_gestion_facturas.dao.CategoriaRepository;
import com.api.gestion.api_gestion_facturas.pojo.Categoria;
import com.api.gestion.api_gestion_facturas.security.jwt.JwtFilter;
import com.api.gestion.api_gestion_facturas.service.CategoriaService;
import com.api.gestion.api_gestion_facturas.util.FacturaUtils;
import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoriaServiceImpl implements CategoriaService{

    private CategoriaRepository categoriaDAO;

    private JwtFilter jwtFilter;

    public CategoriaServiceImpl(CategoriaRepository categoriaDAO, JwtFilter jwtFilter){
        this.categoriaDAO = categoriaDAO;
        this.jwtFilter = jwtFilter;
    }


    @Override
    public ResponseEntity<String> addNuevaCategoria(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()) {
                if(validateCategoriaMap(requestMap, false)){
                    categoriaDAO.save(getCategoriaFromMap(requestMap, false));
                    return FacturaUtils.getResponseEntity("Categoria agregada", HttpStatus.OK);
                }
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    }

    private boolean validateCategoriaMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("nombre")) {
            if(requestMap.containsKey("id") && validateId) {
                return true;
            }
            if(!validateId){
                return true;
            }
        }
        return false;
    }

    private Categoria getCategoriaFromMap(Map<String,String> requestMap, Boolean isAdd) {
        Categoria categoria = new Categoria();
        if(isAdd){
            categoria.setId(Integer.parseInt(requestMap.get("id")));
        }
        categoria.setNombre(requestMap.get("nombre"));
        return categoria;
    }


    @Override
    public ResponseEntity<List<Categoria>> getAllCategorias(String valueFilter) {
        try {
            if(!Strings.isNullOrEmpty(valueFilter) && valueFilter.equalsIgnoreCase("true")){
                log.info("Usando el metodo getAllCategorias() de Categoria");
                return new ResponseEntity<List<Categoria>>(categoriaDAO.getAllCategorias(),HttpStatus.OK);
            }
            log.info("Usando el metodo findAll() de JPARepository");
            return new ResponseEntity<List<Categoria>>(categoriaDAO.findAll(),HttpStatus.OK);
            } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<Categoria>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> updateCategoria(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()) {
                if(validateCategoriaMap(requestMap, true)) {
                    Optional optional = categoriaDAO.findById(Integer.parseInt(requestMap.get("id")));
                    if(optional.isEmpty()) {
                        categoriaDAO.save(getCategoriaFromMap(requestMap, true));
                        return FacturaUtils.getResponseEntity("Categoria actualizada", HttpStatus.OK);
                    }else {
                       return FacturaUtils.getResponseEntity("Categoria no existe", HttpStatus.NOT_FOUND);
                    }
                }
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    }

}
