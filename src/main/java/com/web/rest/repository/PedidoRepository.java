package com.web.rest.repository;

import com.web.rest.model.Pedido;
import com.web.rest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuario(Usuario usuario);

    List<Pedido> findByEstado(Pedido.EstadoPedido estado);
}