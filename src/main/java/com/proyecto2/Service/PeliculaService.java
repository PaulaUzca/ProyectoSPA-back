package com.proyecto2.Service;

import com.proyecto2.DTO.PeliculaDTO;
import com.proyecto2.exception.ResourceNotFoundException;
import com.proyecto2.model.Genero;
import com.proyecto2.model.Pelicula;
import com.proyecto2.model.Usuario;
import com.proyecto2.repository.IGeneroRepository;
import com.proyecto2.repository.IPeliculaRepository;
import com.proyecto2.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeliculaService implements IPeliculaService{

    @Autowired
    private IPeliculaRepository peliculaRepository;
    @Autowired
    private IGeneroRepository generoRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public Pelicula createPelicula(PeliculaDTO peliculaDTO) throws ResourceNotFoundException{
        Genero genero = this.generoRepository.findById(peliculaDTO.getIdGenero()).orElseThrow(() -> new ResourceNotFoundException("Error al buscar el genero"));
        Usuario usuario = this.usuarioRepository.findById(peliculaDTO.getIdCreador()).orElseThrow(() -> new ResourceNotFoundException("Error al buscar el usuario"));
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(peliculaDTO.getTitulo());
        pelicula.setEstreno(peliculaDTO.getEstreno());
        pelicula.setDescripcion(peliculaDTO.getDescripcion());
        pelicula.setImageURL(peliculaDTO.getImageURL());
        pelicula.setIdGenero(peliculaDTO.getIdGenero());
        pelicula.setIdCreador(peliculaDTO.getIdCreador());
        pelicula.setStars(peliculaDTO.getStars());
        pelicula.setGenero(genero);
        pelicula.setCreador(usuario);
        return this.peliculaRepository.save(pelicula);
    }


    @Override
    public Pelicula editPelicula(PeliculaDTO peliculaDTO) throws ResourceNotFoundException{
        Pelicula pelicula = this.peliculaRepository.findById(peliculaDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Error al buscar la pelicula"));
        Genero genero = this.generoRepository.findById(peliculaDTO.getIdGenero()).orElseThrow(() -> new ResourceNotFoundException("Error al buscar el genero"));
        Usuario usuario = this.usuarioRepository.findById(peliculaDTO.getIdCreador()).orElseThrow(() -> new ResourceNotFoundException("Error al buscar el usuario"));
        pelicula.setTitulo(peliculaDTO.getTitulo());
        pelicula.setEstreno(peliculaDTO.getEstreno());
        pelicula.setDescripcion(peliculaDTO.getDescripcion());
        pelicula.setImageURL(peliculaDTO.getImageURL());
        pelicula.setIdGenero(peliculaDTO.getIdGenero());
        pelicula.setStars(peliculaDTO.getStars());
        pelicula.setGenero(genero);
        pelicula.setCreador(usuario);
        return this.peliculaRepository.save(pelicula);
    }
}
