package com.web.rest.service.impl;

import com.web.rest.dto.ProductoDTO;
import com.web.rest.dto.ProductoResponseDTO;
import com.web.rest.dto.ProductoMasVendidoDTO;
import com.web.rest.exception.ResourceNotFoundException;
import com.web.rest.model.Categoria;
import com.web.rest.model.Producto;
import com.web.rest.repository.CategoriaRepository;
import com.web.rest.repository.DetallePedidoRepository; // Importar DetallePedidoRepository
import com.web.rest.repository.ProductoRepository;
import com.web.rest.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- ¡IMPORTAR ESTA ANOTACIÓN!

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository; // <-- INYECTAR DetallePedidoRepository

    private ProductoResponseDTO convertToDto(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setIsbn(producto.getIsbn());
        dto.setTitulo(producto.getTitulo());
        dto.setAutor(producto.getAutor());
        dto.setSinopsis(producto.getSinopsis());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setImagenUrl(producto.getImagenUrl());
        if (producto.getCategoria() != null) {
            dto.setCategoria(new ProductoResponseDTO.CategoriaSimpleDTO(
                    producto.getCategoria().getId(),
                    producto.getCategoria().getNombre()
            ));
        }
        dto.setCreatedAt(producto.getCreatedAt());
        dto.setUpdatedAt(producto.getUpdatedAt());
        dto.setIdCategoria(producto.getCategoria() != null ? producto.getCategoria().getId() : null);
        return dto;
    }

    @Override
    public List<ProductoResponseDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoResponseDTO getProductoById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return convertToDto(producto);
    }

    @Override
    public List<ProductoResponseDTO> getProductosByTitulo(String titulo) {
        return productoRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductoResponseDTO> getProductosByAutor(String autor) {
        return productoRepository.findByAutorContainingIgnoreCase(autor).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductoResponseDTO> getProductosByCategoria(Integer idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + idCategoria);
        }
        return productoRepository.findByCategoria_Id(idCategoria).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductoResponseDTO> buscarProductos(String termino) {
        return productoRepository.findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase(termino, termino)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoResponseDTO saveProducto(ProductoDTO productoDTO) {
        Producto producto;

        if (productoDTO.getId() != null) {
            producto = productoRepository.findById(productoDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + productoDTO.getId()));

            if (!producto.getIsbn().equals(productoDTO.getIsbn())) {
                Optional<Producto> productoConMismoIsbn = productoRepository.findByIsbn(productoDTO.getIsbn());
                if (productoConMismoIsbn.isPresent() && !productoConMismoIsbn.get().getId().equals(productoDTO.getId())) {
                    throw new IllegalArgumentException("Ya existe otro producto con el ISBN: " + productoDTO.getIsbn());
                }
            }
        } else {
            if (productoRepository.findByIsbn(productoDTO.getIsbn()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un producto con el ISBN: " + productoDTO.getIsbn());
            }
            producto = new Producto();
        }

        producto.setIsbn(productoDTO.getIsbn());
        producto.setTitulo(productoDTO.getTitulo());
        producto.setAutor(productoDTO.getAutor());
        producto.setSinopsis(productoDTO.getSinopsis());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setImagenUrl(productoDTO.getImagenUrl());

        Categoria categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + productoDTO.getIdCategoria()));
        producto.setCategoria(categoria);

        Producto savedProducto = productoRepository.save(producto);
        return convertToDto(savedProducto);
    }

    @Override
    @Transactional // <-- ¡AÑADIDA ESTA ANOTACIÓN PARA ASEGURAR ATOMICIDAD EN LA VERIFICACIÓN!
    public void deleteProducto(Integer id) {
        // Primero, verifica si el producto existe
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        // --- ¡LÓGICA DE VERIFICACIÓN DE DEPENDENCIAS APLICADA ANTES DE LA ELIMINACIÓN! ---
        // Verifica si hay detalles de pedido asociados a este producto
        if (detallePedidoRepository.countByProductoId(id) > 0) {
            // Si hay detalles de pedido, lanzamos una excepción con un mensaje claro
            throw new IllegalArgumentException("No se puede eliminar el libro '" + producto.getTitulo() + "' porque tiene ventas asociadas en pedidos.");
        }
        // --------------------------------------------------------------------------------

        // Si no hay ventas asociadas, procede a eliminar el producto
        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoMasVendidoDTO> getTopSellingProducts(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("El límite para los productos más vendidos debe ser mayor que cero.");
        }
        List<ProductoMasVendidoDTO> topProducts = detallePedidoRepository.findTopSellingProductsRaw();
        return topProducts.stream()
                          .limit(limit)
                          .collect(Collectors.toList());
    }
}