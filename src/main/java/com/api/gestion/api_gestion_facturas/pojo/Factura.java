package com.api.gestion.api_gestion_facturas.pojo;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;
@NamedQueries({
    @NamedQuery(name = "Factura.getFacturas", query = "SELECT f FROM Factura f ORDER BY f.id DESC"),
    @NamedQuery(name = "Factura.getFacturasByUsername", query = "SELECT f FROM Factura f WHERE f.createdBy=:username ORDER BY f.id DESC")
})
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "numeroContacto")
    private String numeroContacto;

    @Column(name = "metodoPago")
    private String metodoPago;

    @Column(name = "total")
    private Integer total;

    @Column(name = "productoDetalles", columnDefinition = "json")
    private String productoDetalles;

    @Column(name = "createdBy")
    private String createdBy;
}
