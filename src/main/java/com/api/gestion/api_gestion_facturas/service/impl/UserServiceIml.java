package com.api.gestion.api_gestion_facturas.service.impl;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.api.gestion.api_gestion_facturas.constantes.FacturaConstantes;
import com.api.gestion.api_gestion_facturas.dao.UserRepository;
import com.api.gestion.api_gestion_facturas.pojo.User;
import com.api.gestion.api_gestion_facturas.security.CustomerDetailsService;
import com.api.gestion.api_gestion_facturas.security.jwt.JwtUtil;
import com.api.gestion.api_gestion_facturas.service.UserService;
import com.api.gestion.api_gestion_facturas.util.FacturaUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceIml implements UserService{

    @Autowired
    private UserRepository userDAO;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerDetailsService customerDetailsService;


    
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Registro interno de un usuario", requestMap);
        try{
            if(validateSignUpMap(requestMap)){
                User user = userDAO.findByEmail(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userDAO.save(getUserFromMap(requestMap));
                    return FacturaUtils.getResponseEntity(("Usuario registrado con éxito"), HttpStatus.CREATED);
                } else {
                    return FacturaUtils.getResponseEntity(("El usuario con ese email ya existe"), HttpStatus.BAD_REQUEST);
                }

            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
        if(requestMap.containsKey("nombre") && requestMap.containsKey("numeroDeContacto") && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }else{
            return false;
        }
    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setNombre(requestMap.get("nombre"));
        user.setNumeroDeContacto(requestMap.get("numeroDeContacto"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRol("user");
        return user;
    }

    @Override
    public ResponseEntity<String> Login(Map<String, String> requesMap) {
        log.info("Dentro de Login");
        try{
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requesMap.get("email"), requesMap.get("password"))
            );
            if(authentication.isAuthenticated()){
                if(customerDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+ jwtUtil.generateToken(
                        customerDetailsService.getUserDetail().getEmail(), 
                        customerDetailsService.getUserDetail().getRol())
                    +"\"}", HttpStatus.OK);
                }else {
                    return new ResponseEntity<String>("{\"mensaje\":\""+"Espere la aprobacion del administrador"+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        }catch(Exception e){
            log.info("{}"+e);
        }
        
        return new ResponseEntity<String>("{\"mensaje\":\""+"Credenciales incorrectas "+"\"}",HttpStatus.BAD_REQUEST);
    }
}
