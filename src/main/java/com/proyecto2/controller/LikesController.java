package com.proyecto2.controller;

import com.proyecto2.DTO.LikeDTO;
import com.proyecto2.DTO.PeliculaDTO;
import com.proyecto2.exception.ResourceAlreadyExistsException;
import com.proyecto2.exception.ResourceNotFoundException;
import com.proyecto2.model.Like;
import com.proyecto2.model.Pelicula;
import com.proyecto2.repository.ILikeRepository;
import com.proyecto2.repository.IPeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.text.DecimalFormat;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)

@RestController
@RequestMapping("/api/likes")
public class LikesController {

    @Autowired
    private ILikeRepository likeRepository;
    @Autowired
    private IPeliculaRepository peliculaRepository;

    /** Darle like a una pelicula */
    @PostMapping()
    ResponseEntity<PeliculaDTO> likePelicula(@Valid @RequestBody LikeDTO like) throws Exception {
        Pelicula muvi = peliculaRepository.findById(like.getIdPelicula()).get();
        if(muvi == null){
            throw new ResourceNotFoundException("No se encontro la pelicula");
        }

        if(muvi.getIdCreador() == like.getIdUsuario()){
            throw new Exception("No le puedes dar like a tu propio post");
        }
        if(likeRepository.findByIdUsuarioAndIdPelicula(like.getIdUsuario(), like.getIdPelicula()) != null){
            throw new ResourceAlreadyExistsException("Ya le diste calificaste esta muvi");
        }
        Like newLike = new Like();
        newLike.setIdUsuario(like.getIdUsuario());
        newLike.setIdPelicula(like.getIdPelicula());
        newLike.setHearts(like.getHearts());
        this.likeRepository.save(newLike);
        return new ResponseEntity<>(toPeliculaDTO(muvi), HttpStatus.CREATED);
    }

    /** Obtener like de usuario  a una pelicula*/
    @GetMapping("/findLike")
    ResponseEntity<Float> findUserLike(@RequestParam(name="idUsuario") Long idUsuario,
                                         @RequestParam(name = "idPelicula") Long idPelicula) {
        Like like = this.likeRepository.findByIdUsuarioAndIdPelicula(idUsuario, idPelicula);
        if(like == null){
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(like.getHearts(),HttpStatus.OK);
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

    public LikeDTO toLikeDTO(Like like){
        return new LikeDTO(
                like.getId(),
                like.getIdPelicula(),
                like.getIdUsuario(),
                like.getHearts()
        );
    }
}
