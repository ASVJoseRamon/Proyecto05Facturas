package com.api.gestion.api_gestion_facturas.security;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.gestion.api_gestion_facturas.dao.UserRepository;
import com.api.gestion.api_gestion_facturas.pojo.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerDetailsService implements UserDetailsService{
    
    @Autowired
    private UserRepository userDAO;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Dentro de loadUserByUsername {}", username);    
        User userDetail = userDAO.findByEmail(username);
        log.info("Dentro de loadUserByUsername {}", userDetail.getEmail());    
        if(!Objects.isNull(userDetail)){
            return new org.springframework.security.core.userdetails
            .User(userDetail.getEmail(),
            userDetail.getPassword(), 
            new ArrayList<>());
        }else {
            throw new UsernameNotFoundException("Usuario no encontrado"); 
        }
    }

    public User getUserDetail(String username) {
        User userDetail = userDAO.findByEmail(username);
        return userDetail;
    }
}
