package com.api.gestion.api_gestion_facturas.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.gestion.api_gestion_facturas.constantes.FacturaConstantes;
import com.api.gestion.api_gestion_facturas.pojo.Categoria;
import com.api.gestion.api_gestion_facturas.service.CategoriaService;
import com.api.gestion.api_gestion_facturas.util.FacturaUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping("/add")
    public ResponseEntity<String> agregarNuevaCategoria(@RequestBody(required = true) Map<String,String> requestMap) {
        try {
            return categoriaService.addNuevaCategoria(requestMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Categoria>> listarCategorias(@RequestParam(required = false) String valueFiltrer) {
        try {
            return categoriaService.getAllCategorias(valueFiltrer);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> actualizarCategoria(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            return categoriaService.updateCategoria(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
