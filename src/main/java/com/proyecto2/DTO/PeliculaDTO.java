package com.proyecto2.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeliculaDTO {
    private Long id;
    @NotBlank
    @Length(max = 300)
    private String titulo;
    private Integer estreno;
    @NotBlank
    @Length(max = 1000)
    private String descripcion;
    @NotBlank
    @Length(max = 700)
    private String imageURL;
    @NotNull
    private Long idGenero;
    private String nombreGenero;
    @NotNull
    private Long idCreador;
    private String nombreCreador;
}
