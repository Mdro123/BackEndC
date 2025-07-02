package com.web.rest.controller;

import com.web.rest.dto.RegistroDTO;
import com.web.rest.dto.LoginDTO;
import com.web.rest.dto.LoginResponseDTO;
import com.web.rest.model.Usuario;
import com.web.rest.service.UsuarioService;
import com.web.rest.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody RegistroDTO dto) {
        Usuario nuevoUsuario = usuarioService.registrar(dto);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponseDTO(jwt, "Login exitoso", userDetails.getUsername()));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new LoginResponseDTO(null, "Datos incorrectas"));
        }
    }
}