package com.proyecto2.repository;

import com.proyecto2.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    Usuario findByName(String name);

    @Query(value = "Select U from Usuario U" +
            " Where U.name = :emailOrUsername OR U.email = :emailOrUsername")
    Usuario findByEmailOrUsername(String emailOrUsername);

}
