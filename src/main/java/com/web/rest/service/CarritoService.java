package com.web.rest.service;

import com.web.rest.dto.CarritoValidacionRequestDTO;
import com.web.rest.dto.CarritoValidacionResponseDTO;

public interface CarritoService {

    CarritoValidacionResponseDTO validarCarrito(CarritoValidacionRequestDTO request);
}