package com.web.rest.service.impl;

import com.web.rest.dto.CarritoValidacionRequestDTO;
import com.web.rest.dto.CarritoValidacionResponseDTO;
import com.web.rest.dto.ItemCarritoDTO;
import com.web.rest.dto.ItemValidadoDTO;
import com.web.rest.model.Producto;
import com.web.rest.repository.ProductoRepository;
import com.web.rest.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public CarritoValidacionResponseDTO validarCarrito(CarritoValidacionRequestDTO request) {
        List<ItemValidadoDTO> itemsValidados = new ArrayList<>();
        BigDecimal totalCalculado = BigDecimal.ZERO;
        boolean carritoCompletoValido = true;

        for (ItemCarritoDTO itemDto : request.getItems()) {
            ItemValidadoDTO itemValidado = new ItemValidadoDTO();
            itemValidado.setProductId(itemDto.getProductId());
            itemValidado.setCantidadSolicitada(itemDto.getCantidad());
            itemValidado.setValido(true);

            Optional<Producto> productoOptional = productoRepository.findById(itemDto.getProductId());

            if (productoOptional.isPresent()) {
                Producto producto = productoOptional.get();
                itemValidado.setTitulo(producto.getTitulo());
                itemValidado.setImagenUrl(producto.getImagenUrl());
                itemValidado.setPrecioUnitario(producto.getPrecio());
                itemValidado.setCantidadDisponible(producto.getStock());

                if (itemDto.getCantidad() > producto.getStock()) {
                    itemValidado.setValido(false);
                    itemValidado.setMensaje("Cantidad solicitada excede el stock disponible. Solo hay " + producto.getStock() + " unidades.");
                    carritoCompletoValido = false;
                } else if (itemDto.getCantidad() <= 0) {
                    itemValidado.setValido(false);
                    itemValidado.setMensaje("La cantidad debe ser mayor que cero.");
                    carritoCompletoValido = false;
                } else {
                    totalCalculado = totalCalculado.add(producto.getPrecio().multiply(BigDecimal.valueOf(itemDto.getCantidad())));
                }
            } else {
                itemValidado.setValido(false);
                itemValidado.setMensaje("Producto no encontrado.");
                itemValidado.setCantidadDisponible(0);
                itemValidado.setPrecioUnitario(BigDecimal.ZERO);
                carritoCompletoValido = false;
            }
            itemsValidados.add(itemValidado);
        }

        CarritoValidacionResponseDTO response = new CarritoValidacionResponseDTO();
        response.setItemsValidados(itemsValidados);
        response.setTotalCalculado(totalCalculado);
        response.setCarritoCompletoValido(carritoCompletoValido);

        return response;
    }
}