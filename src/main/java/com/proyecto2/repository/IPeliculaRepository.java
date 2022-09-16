package com.proyecto2.repository;

import com.proyecto2.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPeliculaRepository extends JpaRepository<Pelicula, Long> {

    /**
     * Obtener las peliculas por genero, ... o no
     */
    @Query(value = "Select p from Pelicula p" +
            " Where p.idGenero = :genero OR :genero IS NULL")
    List<Pelicula> findAllByGeneroOrNot(Long genero);

    List<Pelicula> findAllByIdCreador(Long idCreador);

    @Query(value = "Select p from Pelicula p" +
            " Where ( UPPER(TRANSLATE( p.titulo, 'áéíóúÁÉÍÓÚ', 'aeiouAEIOU'))  LIKE UPPER(TRANSLATE( :titulo, 'áéíóúÁÉÍÓÚ', 'aeiouAEIOU')))" +
            " Order by p.titulo")
    List<Pelicula> findAllLikeTitulo(String titulo);
}
