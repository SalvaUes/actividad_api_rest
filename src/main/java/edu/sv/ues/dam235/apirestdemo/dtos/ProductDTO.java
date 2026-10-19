package edu.sv.ues.dam235.apirestdemo.dtos;

import java.math.BigDecimal;

public record ProductDTO(Long id, String nombre, String descripcion,
                         BigDecimal precio, Integer stock, Boolean activo) {}
