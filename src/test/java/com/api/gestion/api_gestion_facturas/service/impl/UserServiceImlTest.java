package com.api.gestion.api_gestion_facturas.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.api.gestion.api_gestion_facturas.dao.UserRepository;
import com.api.gestion.api_gestion_facturas.pojo.User;
import com.api.gestion.api_gestion_facturas.security.CustomerDetailsService;
import com.api.gestion.api_gestion_facturas.security.jwt.JwtUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceImlTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomerDetailsService customerDetailsService;

    @InjectMocks
    private UserServiceIml userService;

    @Test
    void loginShouldReturnTokenWhenUserExistsInDatabase() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "juan@example.com");
        requestMap.put("password", "secret123");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        User user = new User();
        user.setEmail("juan@example.com");
        user.setRol("user");
        user.setStatus("true");

        when(userRepository.findByEmail("juan@example.com")).thenReturn(user);
        when(jwtUtil.generateToken("juan@example.com", "user")).thenReturn("sample-token");

        ResponseEntity<String> response = userService.Login(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("sample-token"));
    }
}
