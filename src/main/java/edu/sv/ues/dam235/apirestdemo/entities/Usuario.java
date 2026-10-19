package edu.sv.ues.dam235.apirestdemo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(nullable = false)
    private String password; // hash BCrypt

    @Column(nullable = false, length = 20)
    private String rol = "USER";

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    void antesDeGuardar() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
    }
}
