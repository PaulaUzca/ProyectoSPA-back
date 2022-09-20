package com.proyecto2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PELICULAS", schema="private")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "titulo", length=300, nullable = false)
    private String titulo;

    @Column(name = "estreno")
    private Integer estreno;

    @Column(name = "descripcion", length=1000)
    private String descripcion;

    @Column(name = "imageURL", length = 700, nullable = false)
    private String imageURL;

    @Column(name = "id_genero", nullable = false)
    private Long idGenero;

    @Column(name = "id_creador", nullable = false)
    private Long idCreador;

    @Column(name = "stars")
    private Float stars;

    @JsonIgnore
    @JoinColumn(name = "id_genero", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Genero genero;

    @JsonIgnore
    @JoinColumn(name = "id_creador", insertable = false, updatable = false)
    @ManyToOne
    private Usuario creador;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pelicula", cascade = CascadeType.REMOVE)
    private List<Like> likeList;
}
