package com.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WishlistItemResponseDTO {
    private Integer id; // ID del ítem en la lista de deseos
    private Integer productoId;
    private String titulo;
    private String autor;
    private BigDecimal precio;
    private String imagenUrl;
    private LocalDateTime addedAt;

    // Constructor vacío
    public WishlistItemResponseDTO() {}

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
