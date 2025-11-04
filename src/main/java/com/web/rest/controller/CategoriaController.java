package com.web.rest.controller;

import com.web.rest.dto.CategoriaDTO;
import com.web.rest.model.Categoria;
import com.web.rest.service.CategoriaService;
import jakarta.validation.Valid; // Asegúrate de tener esta importación
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:4200/")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.getAllCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Integer id) {
        Categoria categoria = categoriaService.getCategoriaById(id);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> getCategoriasByNombre(@RequestParam String nombre) {
        List<Categoria> categorias = categoriaService.getCategoriasByNombre(nombre);
        return ResponseEntity.ok(categorias);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Categoria> createCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        // saveCategoria manejará la creación (ID del DTO es null)
        Categoria nuevaCategoria = categoriaService.saveCategoria(categoriaDTO);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Integer id, @Valid @RequestBody CategoriaDTO categoriaDTO) {
        // Establece el ID en el DTO antes de llamar a saveCategoria
        categoriaDTO.setId(id);
        Categoria categoriaActualizada = categoriaService.saveCategoria(categoriaDTO);
        return ResponseEntity.ok(categoriaActualizada);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
