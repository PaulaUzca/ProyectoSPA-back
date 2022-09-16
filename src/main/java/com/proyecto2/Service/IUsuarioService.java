package com.proyecto2.Service;

import com.proyecto2.DTO.UsuarioDTO;
import com.proyecto2.exception.ResourceAlreadyExistsException;
import com.proyecto2.exception.ResourceNotFoundException;
import com.proyecto2.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public interface IUsuarioService {

    Usuario saveUser(UsuarioDTO usuarioDTO) throws ResourceAlreadyExistsException;

    Usuario logIn(UsuarioDTO usuarioDTO) throws ResourceNotFoundException;

}
