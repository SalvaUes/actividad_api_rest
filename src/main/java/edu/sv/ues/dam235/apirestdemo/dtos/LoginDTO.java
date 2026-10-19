package edu.sv.ues.dam235.apirestdemo.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank String correo, @NotBlank String password) {}
