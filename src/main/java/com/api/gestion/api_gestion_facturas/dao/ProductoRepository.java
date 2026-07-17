package com.api.gestion.api_gestion_facturas.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.gestion.api_gestion_facturas.pojo.Producto;
import com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper;

import jakarta.transaction.Transactional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Integer>{
        List<ProductoWrapper> getAllProductos();
        
        @Transactional
        @Modifying
        Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

        List<Producto> searchByNombre(@Param("nombre") String nombre);

        List<ProductoWrapper> getProductosByCategoria(@Param("id") Integer id);

        ProductoWrapper getProductosById(@Param("id") Integer id);

        List<ProductoWrapper> getProductosByDateASC();

        List<ProductoWrapper> getProductosByDateDESC();
}
