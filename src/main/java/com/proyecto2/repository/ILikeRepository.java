package com.proyecto2.repository;

import com.proyecto2.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILikeRepository extends JpaRepository<Like, Long> {

    List<Like> findAllByIdUsuario(Long idUsuario);

    Like findByIdUsuarioAndIdPelicula(Long idUsuario, Long idPelicula);
}
