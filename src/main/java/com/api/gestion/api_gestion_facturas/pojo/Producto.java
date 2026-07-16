package com.api.gestion.api_gestion_facturas.pojo;

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
import lombok.Data;

@NamedQuery(name = "Producto.getAllProductos",query = "SELECT NEW com.api.gestion.api_gestion_facturas.wrapper.ProductoWrapper(p.id,p.nombre,p.descripcion,p.precio,p.status,p.categoria.id,p.categoria.nombre) FROM Producto p")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "productos")
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
}
