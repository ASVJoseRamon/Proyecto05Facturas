package com.api.gestion.api_gestion_facturas.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.gestion.api_gestion_facturas.constantes.FacturaConstantes;
import com.api.gestion.api_gestion_facturas.service.ProductoService;
import com.api.gestion.api_gestion_facturas.util.FacturaUtils;
import com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/api/v1/producto")
@RestController
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping("/add")
    public ResponseEntity agregarNuevoProducto(@RequestBody Map<String,String> requestMap) {
        try {
            return productoService.addNuevoProducto(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get")
    public ResponseEntity<List<ProductoWrapper>> listarProductos() {
        try {
            return productoService.getAllProductos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @PostMapping("/update")
    public ResponseEntity<String> actualizarProducto(@RequestBody Map<String, String> requestMap) {
        try {
            return productoService.updateProducto(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @PatchMapping("/categoriaId/{categoriaId}/productoId/{id}")
    public ResponseEntity<String> actualizarproductoFilter(@RequestBody Map<String, String> requestMap, @PathVariable Integer categoriaId, @PathVariable Integer id){
        try {
            return productoService.updateProductoFilter(requestMap, categoriaId, id);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return FacturaUtils.getResponseEntity("Ocurrio un error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
