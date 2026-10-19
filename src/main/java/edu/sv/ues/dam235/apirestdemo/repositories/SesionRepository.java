package edu.sv.ues.dam235.apirestdemo.repositories;

import edu.sv.ues.dam235.apirestdemo.entities.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SesionRepository extends JpaRepository<Sesion, Long> {
    Optional<Sesion> findByTokenAndActivaTrue(String token);
}
