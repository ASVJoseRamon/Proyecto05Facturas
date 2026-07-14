package com.api.gestion.api_gestion_facturas.pojo;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;


@NamedQuery(name="User.findByEmail", query = "select u from User u where u.email=:email")
@NamedQuery(name = "User.getAllUsers", query = "SELECT NEW com.api.gestion.api_gestion_facturas.wrapper.UserWrapper(u.id,u.nombre,u.email, u.numeroDeContacto, u.status) FROM User u WHERE u.rol='user'")
@NamedQuery(name = "User.updateStatus", query = "UPDATE User u SET u.status=:status WHERE u.id=:id")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "numeroDeContacto")
    private String numeroDeContacto;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "rol")
    private String rol;
}
