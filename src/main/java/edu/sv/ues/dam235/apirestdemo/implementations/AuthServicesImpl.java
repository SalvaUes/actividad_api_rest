package edu.sv.ues.dam235.apirestdemo.implementations;

import edu.sv.ues.dam235.apirestdemo.dtos.LoginDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.TokenDTO;
import edu.sv.ues.dam235.apirestdemo.entities.Sesion;
import edu.sv.ues.dam235.apirestdemo.entities.Usuario;
import edu.sv.ues.dam235.apirestdemo.repositories.SesionRepository;
import edu.sv.ues.dam235.apirestdemo.repositories.UsuarioRepository;
import edu.sv.ues.dam235.apirestdemo.services.AuthServices;
import edu.sv.ues.dam235.apirestdemo.utilities.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServicesImpl implements AuthServices {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final SesionRepository sesionRepository;
    private final JwtUtil jwtUtil;

    @Override
    public TokenDTO login(LoginDTO request) {
        String correo = request.correo().trim().toLowerCase();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(correo, request.password()));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol());

        Sesion sesion = new Sesion();
        sesion.setUsuario(usuario);
        sesion.setToken(token);
        sesion.setFechaInicio(LocalDateTime.now());
        sesion.setFechaExpiracion(LocalDateTime.now().plus(Duration.ofMillis(jwtUtil.getExpirationMs())));
        sesion.setActiva(true);
        sesionRepository.save(sesion);

        return new TokenDTO(token, "Inicio de sesión exitoso");
    }

    @Override
    @Transactional
    public void logout(String token) {
        sesionRepository.findByTokenAndActivaTrue(token)
                .ifPresent(s -> s.setActiva(false));
    }
}
