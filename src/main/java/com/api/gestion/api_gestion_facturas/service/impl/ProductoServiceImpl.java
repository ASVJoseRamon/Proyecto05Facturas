package com.api.gestion.api_gestion_facturas.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.gestion.api_gestion_facturas.constantes.FacturaConstantes;
import com.api.gestion.api_gestion_facturas.dao.ProductoRepository;
import com.api.gestion.api_gestion_facturas.pojo.Categoria;
import com.api.gestion.api_gestion_facturas.pojo.Producto;
import com.api.gestion.api_gestion_facturas.security.jwt.JwtFilter;
import com.api.gestion.api_gestion_facturas.service.ProductoService;
import com.api.gestion.api_gestion_facturas.util.FacturaUtils;
import com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoDAO;
    private final JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNuevoProducto(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
               if(validateProductoMap(requestMap, false)){
                    productoDAO.save(getProductoFromMap(requestMap, false));
                    return FacturaUtils.getResponseEntity("Producto agregado", HttpStatus.OK);
               }
               return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.BAD_REQUEST); 
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            
    }

    private Producto getProductoFromMap(Map<String, String> requestMap, boolean isAdd) {
        Categoria categoria =  new Categoria();
        categoria.setId(Integer.parseInt(requestMap.get("categoriaId")));
        
        Producto producto = new Producto();
        if(isAdd) {
            producto.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            producto.setStatus("true");
        }

        producto.setCategoria(categoria);
        producto.setNombre(requestMap.get("nombre"));
        producto.setDescripcion(requestMap.get("descripcion"));
        producto.setPrecio(Integer.parseInt(requestMap.get("precio")));
        return producto;
    }

    private Boolean validateProductoMap(Map<String,String> requestMap, boolean validateId){
        if(requestMap.containsKey("nombre")) {
            if(requestMap.containsKey("id") && validateId){
                return true;
            }   
            if(!validateId){
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<List<ProductoWrapper>> getAllProductos() {
        try {
            return new ResponseEntity<>(productoDAO.getAllProductos(),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProducto(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateProductoMap(requestMap, true)){
                    Optional<Producto> productoOptional = productoDAO.findById(Integer.parseInt(requestMap.get("id")));
                    if(!productoOptional.isEmpty()){
                        Producto producto = getProductoFromMap(requestMap, true);
                        producto.setStatus(productoOptional.get().getStatus());
                        productoDAO.save(producto);
                        return FacturaUtils.getResponseEntity("Producto Actualizado", HttpStatus.OK);
                    } else {
                        return FacturaUtils.getResponseEntity("Ese producto no existe", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductoFilter(Map<String, String> requestMap, Integer categoriaId,
            Integer id) {
                try {
                    if(jwtFilter.isAdmin()){
                        if(jwtFilter.isAdmin()){
                            Optional<Producto> optional = productoDAO.findById(id);
                            Optional<Categoria> categorOptional = categoria
                            if (!optional.isEmpty()) {
                                Producto prod = getProductoFromMap(requestMap, true);
                                productoDAO.updateProducto(requestMap);
                                return FacturaUtils.getResponseEntity("Estatus del usuario actualizado", HttpStatus.OK);
                            } else {
                                FacturaUtils.getResponseEntity("El usuario no existe", HttpStatus.NOT_FOUND);
                            }
                        } else {
                            return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
                        }
                    }
                } catch (Exception e) {
                    return FacturaUtils.getResponseEntity("Ocurrio un error en el filtro", HttpStatus.INTERNAL_SERVER_ERROR);    
                }
                return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR); 
    }

}
