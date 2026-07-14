package com.api.gestion.api_gestion_facturas.service.impl;

import com.api.gestion.api_gestion_facturas.security.jwt.JwtFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.gestion.api_gestion_facturas.constantes.FacturaConstantes;
import com.api.gestion.api_gestion_facturas.dao.UserRepository;
import com.api.gestion.api_gestion_facturas.pojo.User;
import com.api.gestion.api_gestion_facturas.security.CustomerDetailsService;
import com.api.gestion.api_gestion_facturas.security.jwt.JwtUtil;
import com.api.gestion.api_gestion_facturas.service.UserService;
import com.api.gestion.api_gestion_facturas.util.FacturaUtils;
import com.api.gestion.api_gestion_facturas.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceIml implements UserService{

    private UserRepository userDAO;
    
    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    private JwtFilter jwtFilter;

    private CustomerDetailsService customerDetailsService;

    private PasswordEncoder passwordEncoder;

    public UserServiceIml(UserRepository userDAO,AuthenticationManager authenticationManager,JwtUtil jwtUtil, CustomerDetailsService customerDetailsService, PasswordEncoder passwordEncoder, JwtFilter jwtFilter){
        this.userDAO = userDAO;
        this.authenticationManager=authenticationManager;
        this.jwtUtil=jwtUtil;
        this.customerDetailsService=customerDetailsService;
        this.passwordEncoder=passwordEncoder;
        this.jwtFilter = jwtFilter;
    }

    
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
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
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
                User user = userDAO.findByEmail(requesMap.get("email"));
                if(user != null && user.getStatus() != null && user.getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+ jwtUtil.generateToken(
                        user.getEmail(),
                        user.getRol())
                    +"\"}", HttpStatus.OK);
                } else if(user != null) {
                    return new ResponseEntity<String>("{\"mensaje\":\""+"Espere la aprobacion del administrador"+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        }catch(Exception e){
            log.info("Error: {}"+e);
        }
        
        return new ResponseEntity<String>("{\"mensaje\":\""+"Credenciales incorrectas "+"\"}",HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDAO.getAllUsers(),HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }   catch (Exception e) {
            e.printStackTrace();
        } 
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                Optional<User> optionalUser = userDAO.findById(Integer.parseInt(requestMap.get("id")));
                if (!optionalUser.isEmpty()) {
                    userDAO.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return FacturaUtils.getResponseEntity("Estatus del usuario actualizado", HttpStatus.OK);
                } else {
                    FacturaUtils.getResponseEntity("El usuario no existe", HttpStatus.NOT_FOUND);
                }
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }    
}
