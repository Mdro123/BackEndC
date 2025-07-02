package com.web.rest.repository;

import com.web.rest.dto.ProductoMasVendidoDTO;
import com.web.rest.model.DetallePedido;
import com.web.rest.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    List<DetallePedido> findByPedido(Pedido pedido);

    @Query("SELECT new com.web.rest.dto.ProductoMasVendidoDTO("
            + "dp.producto.id, dp.producto.isbn, dp.producto.titulo, dp.producto.autor, dp.producto.imagenUrl, dp.producto.precio, SUM(dp.cantidad)) "
            + "FROM DetallePedido dp "
            + "GROUP BY dp.producto.id, dp.producto.isbn, dp.producto.titulo, dp.producto.autor, dp.producto.imagenUrl, dp.producto.precio "
            + "ORDER BY SUM(dp.cantidad) DESC")
    List<ProductoMasVendidoDTO> findTopSellingProducts();

    @Query(value = "SELECT new com.web.rest.dto.ProductoMasVendidoDTO("
            + "dp.producto.id, dp.producto.isbn, dp.producto.titulo, dp.producto.autor, dp.producto.imagenUrl, dp.producto.precio, SUM(dp.cantidad)) "
            + "FROM DetallePedido dp "
            + "GROUP BY dp.producto.id, dp.producto.isbn, dp.producto.titulo, dp.producto.autor, dp.producto.imagenUrl, dp.producto.precio "
            + "ORDER BY SUM(dp.cantidad) DESC")
    List<ProductoMasVendidoDTO> findTopSellingProductsRaw();

    long countByProductoId(Integer productoId);
}