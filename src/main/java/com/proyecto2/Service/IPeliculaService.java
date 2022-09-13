package com.proyecto2.Service;

import com.proyecto2.DTO.PeliculaDTO;
import com.proyecto2.exception.ResourceNotFoundException;
import com.proyecto2.model.Pelicula;
import org.springframework.stereotype.Service;

@Service
public interface IPeliculaService {

    Pelicula createPelicula(PeliculaDTO peliculaDTO) throws ResourceNotFoundException;
    Pelicula editPelicula(PeliculaDTO peliculaDTO) throws ResourceNotFoundException;

}
