package com.ardadev.apimuestra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// Este es un patrón de diseño DTO (Data Transfer Object), en donde utilizamos un objeto como intermediario en la comunicación de otras capas de arquitectura
// el objecto de tipo record nos crea un objeto en tiempo de compilación solamente, parecido a un dataclass en kotlin esto en las nuevas versiones de Java
public record User(
        //@NotNull
        @NotBlank // NotBlank verifica que no sea nulo ni este vacio
        String nombre,
        @NotBlank
        @Email
        @Pattern(regexp = "[a-z]@[a-z].[a-z]")
        String email) {
}
