package com.alfredo.test.apirest.repositories;

import java.util.List;

import com.alfredo.test.apirest.models.Movement;

import org.springframework.data.jpa.repository.JpaRepository;



public interface IMovementRepository extends JpaRepository<Movement, Integer> {

    List<Movement> findByAccount(String account);
    
}
