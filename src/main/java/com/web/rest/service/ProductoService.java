package com.web.rest.service;

import com.web.rest.dto.ProductoDTO;
import com.web.rest.dto.ProductoResponseDTO;
import com.web.rest.dto.ProductoMasVendidoDTO;

import java.util.List;

public interface ProductoService {

    List<ProductoResponseDTO> getAllProductos();
    ProductoResponseDTO getProductoById(Integer id);
    List<ProductoResponseDTO> getProductosByTitulo(String titulo);
    List<ProductoResponseDTO> getProductosByAutor(String autor);
    List<ProductoResponseDTO> getProductosByCategoria(Integer idCategoria);
    List<ProductoResponseDTO> buscarProductos(String termino);

    ProductoResponseDTO saveProducto(ProductoDTO productoDTO);

    void deleteProducto(Integer id);

    List<ProductoMasVendidoDTO> getTopSellingProducts(int limit); 
}