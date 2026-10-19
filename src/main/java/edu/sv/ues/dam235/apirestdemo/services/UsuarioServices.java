package edu.sv.ues.dam235.apirestdemo.services;

import edu.sv.ues.dam235.apirestdemo.dtos.RegisterDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.UsuarioResponseDTO;

public interface UsuarioServices {
    UsuarioResponseDTO registrar(RegisterDTO request);
}
