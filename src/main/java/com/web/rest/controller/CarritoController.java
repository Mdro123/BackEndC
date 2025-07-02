package com.web.rest.controller;

import com.web.rest.dto.CarritoValidacionRequestDTO;
import com.web.rest.dto.CarritoValidacionResponseDTO;
import com.web.rest.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:4200")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @PostMapping("/validar")
    public ResponseEntity<CarritoValidacionResponseDTO> validarCarrito(@Valid @RequestBody CarritoValidacionRequestDTO request) {
        CarritoValidacionResponseDTO response = carritoService.validarCarrito(request);
        return ResponseEntity.ok(response);
    }
}