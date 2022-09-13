package com.proyecto2.repository;

import com.proyecto2.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGeneroRepository extends JpaRepository<Genero, Long> {

    List<Genero> findAllByNombre(String nombre);
}
