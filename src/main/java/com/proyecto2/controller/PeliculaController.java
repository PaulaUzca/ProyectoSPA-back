package com.proyecto2.controller;

import com.proyecto2.DTO.PeliculaDTO;
import com.proyecto2.Service.IPeliculaService;
import com.proyecto2.exception.ResourceAlreadyExistsException;
import com.proyecto2.exception.ResourceNotFoundException;
import com.proyecto2.model.Genero;
import com.proyecto2.model.Like;
import com.proyecto2.model.Pelicula;
import com.proyecto2.repository.IGeneroRepository;
import com.proyecto2.repository.ILikeRepository;
import com.proyecto2.repository.IPeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DecimalFormat;
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
    private IPeliculaRepository peliculaRepository;

    @Autowired
    private ILikeRepository likeRepository;
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
    ResponseEntity<List<PeliculaDTO>> getAllPeliculasByCreador(@PathVariable(name="id", required = false) Long id){
        List<Pelicula> peliculaList = this.peliculaRepository.findAllByIdCreador(id);
        if(peliculaList.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        List<PeliculaDTO> peliculaDTOList = peliculaList.stream().map(this::toPeliculaDTO).collect(Collectors.toList());
        return new ResponseEntity<>(peliculaDTOList, HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<List<PeliculaDTO>> searchAllPeliculas(@RequestParam(name = "titulo") String titulo){
        List<Pelicula> peliculaList = this.peliculaRepository.findAllLikeTitulo('%' + titulo.trim() + '%');
        if(peliculaList.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        List<PeliculaDTO> peliculaDTOList = peliculaList.stream().map(this::toPeliculaDTO).collect(Collectors.toList());
        return new ResponseEntity<>(peliculaDTOList, HttpStatus.OK);
    }


    /** Get usuario liked muvis*/
    @GetMapping("/usuarioLiked/{id}")
    ResponseEntity<List<PeliculaDTO>> getLikedByUser(@PathVariable(name = "id") Long idUsuario){
        List<Like> likeList = this.likeRepository.findAllByIdUsuario(idUsuario);
        if(likeList.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        List<PeliculaDTO> peliculaDTOList  = likeList.stream().map(p -> toPeliculaDTO(p.getPelicula())).collect(Collectors.toList());
        return new ResponseEntity<>(peliculaDTOList, HttpStatus.OK);
    }

    // Un maper para pasar a PeliculaDTO
    public PeliculaDTO toPeliculaDTO(Pelicula film) {
        Double avg = null;
        DecimalFormat value = new DecimalFormat("#.#");

        if(film.getLikeList() != null && !film.getLikeList().isEmpty()) {
            avg = film.getLikeList().stream().map(Like::getHearts).mapToDouble(a -> a).average().getAsDouble();
        }
    return new PeliculaDTO(
            film.getId(),
            film.getTitulo(),
            film.getEstreno(),
            film.getDescripcion(),
            film.getImageURL(),
            film.getIdGenero(),
            film.getGenero().getNombre(),
            film.getIdCreador(),
            film.getCreador().getName(),
            film.getStars(),
            film.getLikeList() != null && !film.getLikeList().isEmpty() ?  value.format(avg) : "0",
            film.getLikeList() != null && !film.getLikeList().isEmpty()? film.getLikeList().size() : 0);
    }

    @Autowired
    public void setPeliculaRepository(IPeliculaRepository peliculaRepository){
        this.peliculaRepository = peliculaRepository;
    }
}
