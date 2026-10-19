package edu.sv.ues.dam235.apirestdemo.configs;

import edu.sv.ues.dam235.apirestdemo.entities.Usuario;
import edu.sv.ues.dam235.apirestdemo.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByCorreo(correo.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.withUsername(u.getCorreo())
                .password(u.getPassword())
                .roles(u.getRol())
                .build();
    }
}
