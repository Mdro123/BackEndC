package com.web.rest.service;

import com.web.rest.dto.ConfirmarCompraRequestDTO;
import com.web.rest.dto.PedidoResponseDTO;
import com.web.rest.model.Pedido;

import java.util.List;

public interface PedidoService {

    PedidoResponseDTO confirmarCompra(ConfirmarCompraRequestDTO request, Integer userId);

    List<PedidoResponseDTO> getAllPedidos();
    PedidoResponseDTO getPedidoById(Integer pedidoId);
    List<PedidoResponseDTO> getPedidosByUserId(Integer userId);
    PedidoResponseDTO actualizarEstadoPedido(Integer pedidoId, String nuevoEstado, Integer adminUserId);
    PedidoResponseDTO cancelarPedidoCliente(Integer pedidoId, Integer userId);
}