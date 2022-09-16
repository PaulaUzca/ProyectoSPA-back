package com.proyecto2.controller;

import com.proyecto2.DTO.UsuarioDTO;
import com.proyecto2.Service.IUsuarioService;
import com.proyecto2.exception.ResourceAlreadyExistsException;
import com.proyecto2.exception.ResourceNotFoundException;
import com.proyecto2.model.Usuario;
import com.proyecto2.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
// Esto es para que se pueda tener front y back en un mismo servidor @CrossOrigin(parametros)
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/all")
    List<Usuario> getAllPersonas(){
        return usuarioRepository.findAll();
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> savePersona(@Valid @RequestBody UsuarioDTO usuarioDTO) throws ResourceAlreadyExistsException {
        Usuario usuario = this.usuarioService.saveUser(usuarioDTO);
        return new ResponseEntity<>(new UsuarioDTO(usuario.getId(), usuario.getName(), usuario.getEmail(), null), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@Valid @RequestBody UsuarioDTO usuarioDTO) throws ResourceNotFoundException {
        Usuario foundUser = this.usuarioService.logIn(usuarioDTO);
        return new ResponseEntity<>(new UsuarioDTO(foundUser.getId(), foundUser.getName(), foundUser.getEmail(), null), HttpStatus.OK);
    }

}
