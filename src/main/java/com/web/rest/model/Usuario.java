package com.web.rest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.web.rest.validator.ValidBirthDate;

import java.time.LocalDate;
import java.util.Collection; 
import java.util.Collections; 

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority; 
import org.springframework.security.core.authority.SimpleGrantedAuthority; 
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotBlank(message = "El campo es obligatorio.")
    @Size(min = 3, max = 30, message = "Debe tener entre 3 y 30 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$", message = "No se permiten espacios, números ni caracteres especiales.")
    @Column(nullable = false, length = 30)
    private String nombres;

    @NotBlank(message = "El campo es obligatorio.")
    @Size(min = 3, max = 30, message = "Debe tener entre 3 y 30 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "No se permiten números ni caracteres especiales.")
    @Column(nullable = false, length = 30)
    private String apellidos; 

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 20)
    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento;

    @NotBlank(message = "El campo es obligatorio.")
    @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 dígitos.")
    @Pattern(regexp = "\\d{8}", message = "Solo se permiten números.")
    @Column(name = "numero_documento", unique = true, nullable = false, length = 8)
    private String numeroDocumento;

    @NotBlank(message = "El campo es obligatorio.")
    @Size(min = 9, max = 9, message = "El teléfono debe tener 9 dígitos.")
    @Pattern(regexp = "\\d{9}", message = "Solo se permiten números.")
    @Column(nullable = false, length = 9)
    private String telefono;

    @NotNull(message = "La fecha de nacimiento es obligatoria") 
    @Past(message = "La fecha de nacimiento no puede ser una fecha futura.")
    @ValidBirthDate
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotBlank(message = "Campo obligatorio.")
    @Email(message = "Correo electrónico inválido.")
    @Size(max = 50, message = "El correo excede el límite de 50 caracteres.")
    @Column(unique = true, nullable = false, length = 50) 
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @JsonIgnore
    @Column(nullable = false, length = 60)
    private String password;

    @Enumerated(EnumType.STRING) 
    @Column(length = 50, nullable = false) 
    private Role type; 

    public Usuario() {}

    @PrePersist
    protected void onCreate() {
        if (this.type == null) {
            this.type = Role.CLIENTE;
        }
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getType() { return type; } 
    public void setType(Role type) { this.type = type; } 

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + type.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Podrías implementar lógica de expiración de cuenta si es necesario
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Podrías implementar lógica de bloqueo de cuenta si es necesario
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Podrías implementar lógica de expiración de credenciales si es necesario
    }

    @Override
    public boolean isEnabled() {
        return true; // Podrías implementar lógica de activación de cuenta (ej. por email) si es necesario
    }
    
}