package edu.sv.ues.dam235.apirestdemo.configs;

import edu.sv.ues.dam235.apirestdemo.repositories.SesionRepository;
import edu.sv.ues.dam235.apirestdemo.utilities.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final SesionRepository sesionRepository;

    public JwtFilter(JwtUtil jwtUtil, SesionRepository sesionRepository) {
        this.jwtUtil = jwtUtil;
        this.sesionRepository = sesionRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String token = JwtUtil.limpiarHeader(request.getHeader("Authorization"));

        if (token != null) {
            try {
                Claims claims = jwtUtil.parse(token);
                if (sesionRepository.findByTokenAndActivaTrue(token).isPresent()) {
                    String rol = claims.get("rol", String.class);
                    var auth = new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + rol)));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException | IllegalArgumentException e) {
                // Token inválido o vencido: seguimos sin autenticar -> el entry point responde 401
            }
        }
        chain.doFilter(request, response);
    }
}
