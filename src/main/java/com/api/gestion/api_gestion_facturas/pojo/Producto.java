package com.api.gestion.api_gestion_facturas.pojo;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.NamedQueries;
import lombok.Data;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "productos")
@NamedQueries({
    @NamedQuery(name = "Producto.getAllProductos",query = "SELECT NEW com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper(p.id,p.nombre,p.descripcion,p.precio,p.fechaCreacion,p.status,p.categoria.id,p.categoria.nombre) FROM Producto p"),
    @NamedQuery(name = "Producto.updateStatus", query = "UPDATE Producto p SET p.status=:status WHERE p.id=:id"),
    @NamedQuery(name = "Producto.searchByName", query = "SELECT com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper(p.id,p.nombre,p.descripcion,p.precio,p.fechaCreacion,p.status,p.categoria.id,p.categoria.nombre) FROM Producto p WHERE p.nombre=:nombre"),
    @NamedQuery(name = "Producto.getProductosByCategoria",query = "SELECT NEW com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper(p.id,p.nombre,p.descripcion,p.precio,p.fechaCreacion,p.status,p.categoria.id,p.categoria.nombre) FROM Producto p WHERE p.categoria.id=:id AND p.status='true'"),
    @NamedQuery(name = "Producto.getProductosById",query = "SELECT NEW com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper(p.id,p.nombre,p.descripcion,p.precio,p.fechaCreacion,p.status,p.categoria.id,p.categoria.nombre) FROM Producto p WHERE p.id=:id AND p.status='true'"),
    @NamedQuery(name = "Producto.getProductosByDateASC", query = "SELECT NEW com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper(p.id,p.nombre,p.descripcion,p.precio,p.fechaCreacion,p.status,p.categoria.id,p.categoria.nombre) FROM Producto p ORDER BY p.fechaCreacion ASC"),
    @NamedQuery(name = "Producto.getProductosByDateDESC",query = "SELECT NEW com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper(p.id,p.nombre,p.descripcion,p.precio,p.fechaCreacion,p.status,p.categoria.id,p.categoria.nombre) FROM Producto p ORDER BY p.fechaCreacion DESC"),
})
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_fk", nullable = false)
    private Categoria categoria;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Integer precio;

    @Column(name = "status")
    private String status;
   
    @CreationTimestamp 
    @Column(name = "fecha_creacion", updatable = false, nullable = false)
    private LocalDateTime fechaCreacion;
}
