package com.web.rest.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductoDTO {

    private Integer id;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @Pattern(regexp = ".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*", message = "El título debe contener al menos una letra.")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 3, max = 50, message = "El autor debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = ".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*", message = "El autor debe contener al menos una letra.")
    private String autor;

    @NotBlank(message = "El ISBN es obligatorio")
    @Size(min = 13, max = 13, message = "El ISBN debe tener 13 dígitos.")
    @Pattern(regexp = "^\\d{13}$", message = "El ISBN solo debe contener números.")
    private String isbn;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero.")
    private BigDecimal precio; 

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Integer stock;

    @NotNull(message = "La categoría es obligatoria")
    private Integer idCategoria;

    @NotBlank(message = "La URL de la imagen es obligatoria.") 
    @Pattern(regexp = "^(https?://\\S+)\\.(jpg|jpeg|png)$", message = "URL inválida (sin espacios, con http/https, y terminar en .jpg, .jpeg o .png)")
    private String imagenUrl;
    
    @NotBlank(message = "La sinopsis es obligatoria.") 
    @Size(max = 500, message = "La sinopsis no puede exceder los 500 caracteres.")
    @Pattern(regexp = ".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*", message = "La sinopsis debe contener al menos una letra.")
    private String sinopsis;

    public ProductoDTO() {
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
}
