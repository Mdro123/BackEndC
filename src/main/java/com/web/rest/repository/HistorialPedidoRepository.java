package com.web.rest.repository;

import com.web.rest.model.HistorialPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialPedidoRepository extends JpaRepository<HistorialPedido, Integer> {
}