package edu.sv.ues.dam235.apirestdemo.implementations;

import edu.sv.ues.dam235.apirestdemo.dtos.RegisterDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.UsuarioResponseDTO;
import edu.sv.ues.dam235.apirestdemo.entities.Usuario;
import edu.sv.ues.dam235.apirestdemo.repositories.UsuarioRepository;
import edu.sv.ues.dam235.apirestdemo.services.UsuarioServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UsuarioServicesImpl implements UsuarioServices {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDTO registrar(RegisterDTO request) {
        String correo = request.correo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        Usuario u = new Usuario();
        u.setNombre(request.nombre().trim());
        u.setCorreo(correo);
        u.setPassword(passwordEncoder.encode(request.password())); // BCrypt
        u.setRol("USER");
        Usuario guardado = usuarioRepository.save(u);

        return new UsuarioResponseDTO(guardado.getId(), guardado.getNombre(),
                guardado.getCorreo(), "Usuario creado exitosamente");
    }
}
