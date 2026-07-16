package com.api.gestion.api_gestion_facturas.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoWrapper {
    
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private String status;
    private Integer categoriaId;
    private String nombreCategoria;
}
