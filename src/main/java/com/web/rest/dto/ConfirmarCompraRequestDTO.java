package com.web.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public class ConfirmarCompraRequestDTO {

    @NotNull
    private Integer idMetodoPago;

    @NotEmpty
    @Valid
    private List<ItemCarritoDTO> items;

    private String paymentMethodId;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 5, max = 50, message = "El nombre debe tener entre 5 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "Ingrese un nombre válido (sin números o c. especiales).")
    private String nombre; 

    @NotBlank(message = "Campo obligatorio.")
    @Size(min = 10, max = 100, message = "Debe tener entre 10 y 100 caracteres.")
    @Pattern(regexp = ".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*", message = "Ingrese una dirección válida.")
    private String direccion;

    @NotBlank(message = "Campo obligatorio.")
    @Size(min = 3, max = 30, message = "Debe tener entre 3 y 30 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "Ingrese una ciudad válida.")
    private String ciudad;

    @NotBlank(message = "Campo obligatorio.")
    @Size(min = 5, max = 5, message = "El código postal debe tener 5 dígitos.")
    @Pattern(regexp = "^\\d{5}$", message = "El código postal solo debe contener números.")
    private String codigoPostal; 

    public Integer getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(Integer idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public List<ItemCarritoDTO> getItems() { return items; }
    public void setItems(List<ItemCarritoDTO> items) { this.items = items; }
    public String getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
}