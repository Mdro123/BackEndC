package com.web.rest.service;

import org.springframework.stereotype.Service;

import com.web.rest.dto.RegistroDTO;
import com.web.rest.model.Usuario;

@Service
public interface UsuarioService {
    Usuario registrar(RegistroDTO dto);
    Usuario buscarPorEmail(String email);
    Usuario validarCredenciales(String email, String password); 
}