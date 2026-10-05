package com.huellitas.vetTurno.service;

import com.huellitas.vetTurno.dto.AuthResponse;
import com.huellitas.vetTurno.dto.LoginRequest;
import com.huellitas.vetTurno.dto.RegistroRequest;
import com.huellitas.vetTurno.model.Rol;
import com.huellitas.vetTurno.model.Usuario;
import com.huellitas.vetTurno.repository.UsuarioRepository;
import com.huellitas.vetTurno.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Rol.USER); // siempre USER, sin importar lo que envíe el cliente
        usuarioRepository.save(usuario);

        return new AuthResponse(jwtService.generarToken(usuario.getEmail(), usuario.getRol().name()));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        return new AuthResponse(jwtService.generarToken(usuario.getEmail(), usuario.getRol().name()));
    }
}
