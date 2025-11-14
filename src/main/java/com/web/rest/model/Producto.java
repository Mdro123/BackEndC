package com.web.rest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El ISBN es obligatorio")
    @Size(max = 20, message = "El ISBN no puede exceder los 20 caracteres")
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede exceder los 255 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 100, message = "El autor no puede exceder los 100 caracteres")
    @Column(nullable = false)
    private String autor;

    @Column(columnDefinition = "TEXT")
    private String sinopsis;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener hasta 10 dígitos enteros y 2 decimales")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @Size(max = 255, message = "La URL de la imagen no puede exceder los 255 caracteres")
    @Column(name = "imagen_url")
    private String imagenUrl;

    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Producto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // --- MÉTODO NUEVO AÑADIDO (FASE VERDE) ---

    /**
     * Lógica de negocio TDD para determinar si el producto es elegible para envío gratis.
     * Este método NO se mapea a la base de datos (no tiene @Column).
     *
     * @return true si el precio es S/ 100.00 o más, false en otro caso.
     */
    public boolean isElegibleParaEnvioGratis() {
        // 1. Maneja el caso de prueba "testProductoSinPrecio"
        if (this.precio == null) {
            return false;
        }

        // 2. Define el umbral de envío gratis
        BigDecimal umbralEnvioGratis = new BigDecimal("100.00");
        
        // 3. Compara el precio. (>= 0 significa "mayor o igual que")
        //    Esto satisface las pruebas de 50 (falso), 150 (verdadero) y 100 (verdadero).
        return this.precio.compareTo(umbralEnvioGratis) >= 0;
    }
}
