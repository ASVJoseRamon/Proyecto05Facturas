package com.api.gestion.api_gestion_facturas.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWrapper {
    private Integer id;
    private String nombre;
    private String email;
    private String numeroDeContacto;
    private String status;

}
