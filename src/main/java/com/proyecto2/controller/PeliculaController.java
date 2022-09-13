package com.proyecto2.controller;

import com.proyecto2.DTO.PeliculaDTO;
import com.proyecto2.Service.IPeliculaService;
import com.proyecto2.exception.ResourceAlreadyExistsException;
import com.proyecto2.exception.ResourceNotFoundException;
import com.proyecto2.model.Genero;
import com.proyecto2.model.Pelicula;
import com.proyecto2.repository.IGeneroRepository;
import com.proyecto2.repository.IPeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/movies")
public class PeliculaController {

    @Autowired
    private IPeliculaService peliculaService;
    @Autowired
    private IGeneroRepository generoRepository;
    @Autowired
    private IPeliculaRepository peliculaRepository;
    /**
     * Obtener todos los generos de las peliculas
     */
    @GetMapping("/generos")
    List<Genero> getGeneros(){
       return this.generoRepository.findAll();
    }

    /**
     * Crear un genero
     */
    @PostMapping("/generos/add/{genero}")
    ResponseEntity<Genero> createGenero(@PathVariable String genero) throws ResourceAlreadyExistsException{
        List<Genero> similares = this.generoRepository.findAllByNombre(genero.toUpperCase(Locale.ROOT));
        if(similares != null && !similares.isEmpty()){
            throw new ResourceAlreadyExistsException("Genero ya existe");
        }
        Genero newGenero = new Genero();
        newGenero.setNombre(genero.toUpperCase(Locale.ROOT));
        return new ResponseEntity<>(this.generoRepository.save(newGenero), HttpStatus.CREATED);
    }

    /**
     * Agregar una pelicula
     */
    @PostMapping("/add")
    ResponseEntity<PeliculaDTO> createMovie(@Valid @RequestBody PeliculaDTO peliculaDTO) throws ResourceNotFoundException {
        Pelicula newPelicula = this.peliculaService.createPelicula(peliculaDTO);
        return new ResponseEntity<>(toPeliculaDTO(newPelicula), HttpStatus.CREATED);
    }

    /**
     * Editar una pelicula
     */
    @PutMapping("/edit")
    ResponseEntity<PeliculaDTO> editMovie(@Valid @RequestBody PeliculaDTO peliculaDTO) throws ResourceNotFoundException {
        Pelicula newPelicula = this.peliculaService.editPelicula(peliculaDTO);
        return new ResponseEntity<>(toPeliculaDTO(newPelicula), HttpStatus.CREATED);
    }

    /** Eliminar una pelicula */
    @DeleteMapping("/delete/{id}")
    Boolean deleteMovie(@PathVariable(value = "id") Long id){
        this.peliculaRepository.deleteById(id);
        return true;
    }

    /**
     * Obtener las peliculas
     */
    @GetMapping("/all")
    ResponseEntity<List<PeliculaDTO>> getAllPeliculas(@RequestParam(name="genero", required = false) Long genero){
        List<Pelicula> peliculaList = this.peliculaRepository.findAllByGeneroOrNot(genero);
        if(peliculaList.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        List<PeliculaDTO> peliculaDTOList = peliculaList.stream().map(this::toPeliculaDTO).collect(Collectors.toList());
        return new ResponseEntity<>(peliculaDTOList, HttpStatus.OK);
    }

    /** Obtener una pelicula por su id
     */
    @GetMapping("id/{id}")
    ResponseEntity<PeliculaDTO> getPeliculaById(@PathVariable(value="id") Long id) throws ResourceNotFoundException {
        Pelicula film = this.peliculaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe la pelicula con el id"));
        return new ResponseEntity<>(toPeliculaDTO(film), HttpStatus.OK);
    }

    /**
     * Obtener una pelicula por su creador
     */
    @GetMapping("creador/id/{id}")
    ResponseEntity<List<PeliculaDTO>> getAllPeliculasByCreador(@RequestParam(name="id", required = false) Long id){
        List<Pelicula> peliculaList = this.peliculaRepository.findAllByIdCreador(id);
        if(peliculaList.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        List<PeliculaDTO> peliculaDTOList = peliculaList.stream().map(this::toPeliculaDTO).collect(Collectors.toList());
        return new ResponseEntity<>(peliculaDTOList, HttpStatus.OK);
    }


    // Un maper para pasar a PeliculaDTO
    private PeliculaDTO toPeliculaDTO(Pelicula film) {
    return new PeliculaDTO(
            film.getId(),
            film.getTitulo(),
            film.getEstreno(),
            film.getDescripcion(),
            film.getImageURL(),
            film.getIdGenero(),
            film.getGenero().getNombre(),
            film.getIdCreador(),
            film.getCreador().getName());
    }

}
