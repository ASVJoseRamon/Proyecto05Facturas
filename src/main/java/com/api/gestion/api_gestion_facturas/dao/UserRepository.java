package com.api.gestion.api_gestion_facturas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.gestion.api_gestion_facturas.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(@Param(("email")) String email);
}
