package com.api.gestion.api_gestion_facturas.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Locale.Category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.gestion.api_gestion_facturas.constantes.FacturaConstantes;
import com.api.gestion.api_gestion_facturas.dao.CategoriaRepository;
import com.api.gestion.api_gestion_facturas.dao.ProductoRepository;
import com.api.gestion.api_gestion_facturas.pojo.Categoria;
import com.api.gestion.api_gestion_facturas.pojo.Producto;
import com.api.gestion.api_gestion_facturas.security.jwt.JwtFilter;
import com.api.gestion.api_gestion_facturas.service.ProductoService;
import com.api.gestion.api_gestion_facturas.util.FacturaUtils;
import com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoDAO;
    private final CategoriaRepository categoriaDAO;
    private final JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNuevoProducto(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                requestMap.put("fechaCreacion", null);
                Producto prod = getProductoFromMap(requestMap, false);
                log.info("Producto a agregar: "+prod);

                if (validateProductoMap(requestMap, false)) {
                    log.info("Validacion pasada");

                    productoDAO.save(prod);
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
        Categoria categoria = new Categoria();
        categoria.setId(Integer.parseInt(requestMap.get("categoriaId")));

        Producto producto = new Producto();
        if (isAdd) {
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

    private Boolean validateProductoMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("nombre")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            }
            if (!validateId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<List<ProductoWrapper>> getAllProductos() {
        try {
            return new ResponseEntity<>(productoDAO.getAllProductos(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProducto(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductoMap(requestMap, true)) {
                    Optional<Producto> productoOptional = productoDAO.findById(Integer.parseInt(requestMap.get("id")));
                    if (!productoOptional.isEmpty()) {
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
                    if(jwtFilter.isAdmin()) {
                            log.info("idprod: "+id);
                            log.info("idcat: "+categoriaId);
                            log.info("idcat: "+String.valueOf(requestMap));

                            Optional<Producto> optional = productoDAO.findById(id);
                            Optional<Categoria> categoriaOptional = categoriaDAO.findById(categoriaId);
                            log.info(String.valueOf(optional));
                            log.info(String.valueOf(categoriaOptional));
                            if (!optional.isEmpty() && !categoriaOptional.isEmpty()) {
                                log.info("Optionals no vacios");

                                Producto prod = new Producto();
                                prod = productoDAO.findById(id).orElseThrow( () -> new RuntimeException("Producto no encontrado"));
                                log.info("prod: "+String.valueOf(prod));

                                if(requestMap.containsKey("nombre")){
                                    prod.setNombre(requestMap.get("nombre"));
                                }
                                if(requestMap.containsKey("descripcion")){
                                    prod.setDescripcion(requestMap.get("descripcion"));
                                }
                                if(requestMap.containsKey("precio")){
                                    prod.setPrecio(Integer.parseInt(requestMap.get("precio")));
                                }
                                Categoria cat = categoriaDAO.findById(categoriaId).orElseThrow( () -> new RuntimeException("Categoria No encontrada"));
                                prod.setCategoria(cat);
                                log.info("prod: "+String.valueOf(prod));
                                productoDAO.save(prod);

                                return FacturaUtils.getResponseEntity("Estatus del usuario actualizado", HttpStatus.OK);
                            } else {
                                FacturaUtils.getResponseEntity("El usuario no existe", HttpStatus.NOT_FOUND);
                            }
                        
                        } else {
                            return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
                        }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return FacturaUtils.getResponseEntity("Error en Implementacion", HttpStatus.INTERNAL_SERVER_ERROR); 
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if(jwtFilter.isAdmin()){
                Optional productoOptional = productoDAO.findById(id);
                if(!productoOptional.isEmpty()){
                    log.info("Antes de eliminacion");
                    productoDAO.deleteById(id);
                    log.info("Despues de eliminacion");
                    return FacturaUtils.getResponseEntity("Producto Eliminado con éxito", HttpStatus.OK);
                } else {
                    return FacturaUtils.getResponseEntity("Id vacio", HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);        
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity("Error en Implementacion", HttpStatus.INTERNAL_SERVER_ERROR);  
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()) {
                Optional productoOptional =productoDAO.findById(Integer.parseInt(requestMap.get("id")));
                if(!productoOptional.isEmpty()) {
                    productoDAO.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return FacturaUtils.getResponseEntity("Status actualizado", HttpStatus.OK);
                } else {
                    return FacturaUtils.getResponseEntity("Producto no existe", HttpStatus.NOT_FOUND);
                }
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);  
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity("Error en Implementacion", HttpStatus.INTERNAL_SERVER_ERROR);  
    }

    @Override
    public ResponseEntity<List<Producto>> searchByName(String name) {
        try {
            return new ResponseEntity<List<Producto>>(productoDAO.searchByNombre(name), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoWrapper>> searchByCategoria(Integer id) {
        try {
            return new ResponseEntity<List<ProductoWrapper>>(productoDAO.getProductosByCategoria(id),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductoWrapper> getProdById(Integer id) {
        try {
            return new ResponseEntity<ProductoWrapper>(productoDAO.getProductosById(id),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductoWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoWrapper>> getProductosByDateASC() {
        try {
            return new ResponseEntity<List<ProductoWrapper>>(productoDAO.getProductosByDateASC(),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoWrapper>> getProductosByDateDESC() {
        try {
            return new ResponseEntity<List<ProductoWrapper>>(productoDAO.getProductosByDateDESC(),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
