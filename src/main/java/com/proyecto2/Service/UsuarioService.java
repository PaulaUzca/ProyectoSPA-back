package com.proyecto2.Service;

import com.proyecto2.DTO.UsuarioDTO;
import com.proyecto2.exception.ResourceAlreadyExistsException;
import com.proyecto2.exception.ResourceNotFoundException;
import com.proyecto2.model.Usuario;
import com.proyecto2.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService{

    private IUsuarioRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UsuarioDTO usuarioDTO) throws ResourceAlreadyExistsException {
        Usuario emailExistente = this.userRepository.findByEmail(usuarioDTO.getEmail());
        if(emailExistente != null && emailExistente.getEmail() != null && !emailExistente.getEmail().isEmpty()){
            throw new ResourceAlreadyExistsException("Email ya esta en uso");
        }
        Usuario nombreExistente = this.userRepository.findByName(usuarioDTO.getUsername());
        if(nombreExistente != null && nombreExistente.getName() != null && !nombreExistente.getName().isEmpty()){
            throw new ResourceAlreadyExistsException("Usuario ya esta en uso");
        }

        Usuario user = new Usuario();
        user.setName(usuarioDTO.getUsername());
        user.setEmail(usuarioDTO.getEmail());
        user.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Usuario logIn(UsuarioDTO usuarioDTO) throws ResourceNotFoundException {
        Usuario foundUser = this.userRepository.findByEmailOrUsername(usuarioDTO.getUsername());
        if(foundUser == null){
            throw new ResourceNotFoundException("Email o username incorrectos");
        }
        if(!passwordEncoder.matches(usuarioDTO.getPassword(), foundUser.getPassword())){
            throw new ResourceNotFoundException("Contraseña incorrecta");
        }
        return foundUser;
    }

    @Autowired
    public void setUserRepository(IUsuarioRepository usuarioRepository){
        this.userRepository = usuarioRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
}
