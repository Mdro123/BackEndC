package com.web.rest.service;

import com.web.rest.dto.MetodoPagoDTO;
import com.web.rest.model.MetodoPago;

import java.util.List;

public interface MetodoPagoService {

    List<MetodoPago> getAllMetodosPago();
    MetodoPago getMetodoPagoById(Integer id);
    MetodoPago saveMetodoPago(MetodoPagoDTO metodoPagoDTO);

    void deleteMetodoPago(Integer id);
}