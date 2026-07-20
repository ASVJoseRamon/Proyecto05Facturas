package com.api.gestion.api_gestion_facturas.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.gestion.api_gestion_facturas.constantes.FacturaConstantes;
import com.api.gestion.api_gestion_facturas.pojo.Factura;
import com.api.gestion.api_gestion_facturas.service.FacturaService;
import com.api.gestion.api_gestion_facturas.util.FacturaUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/factura")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping("/generarReporte")
    ResponseEntity<String> generarReporte(@RequestBody Map<String, Object> requestMap) {
        try {
            return facturaService.generateReport(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getFacturas")
    public ResponseEntity<List<Factura>> listarFacturas() {
        try {
            return facturaService.getFacturas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/getPDF")
    public ResponseEntity<byte[]> obtenerPDF(@RequestBody Map<String,Object> requestMap){
        try {
            return facturaService.getPDF(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/eliminarFactura/{id}")
    public ResponseEntity<String> eliminarFactura(@PathVariable Integer id) {
        try {
            return facturaService.deleteFactura(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
