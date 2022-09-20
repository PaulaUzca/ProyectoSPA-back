package com.proyecto2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "LIKES", schema = "private")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "hearts", nullable = false)
    private Float hearts;

    @Column(name = "id_pelicula", nullable = false)
    private Long idPelicula;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @JsonIgnore
    @JoinColumn(name = "id_pelicula", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Pelicula pelicula;

    @JsonIgnore
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
}
