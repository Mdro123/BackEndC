package com.web.rest.service.impl;

import com.web.rest.dto.CategoriaDTO;
import com.web.rest.exception.ResourceNotFoundException;
import com.web.rest.model.Categoria;
import com.web.rest.repository.CategoriaRepository;
import com.web.rest.repository.ProductoRepository; // Asegúrate de que esta importación exista
import com.web.rest.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importa Transactional

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository; // Necesario para la validación al borrar

    @Override
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria getCategoriaById(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
    }

    @Override
    public List<Categoria> getCategoriasByNombre(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // --- MÉTODO SEPARADO PARA CREAR ---
    @Override
    public Categoria createCategoria(CategoriaDTO categoriaDTO) {
        // Verificar si ya existe una categoría con el mismo nombre
        if (categoriaRepository.findByNombreIgnoreCase(categoriaDTO.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    // --- MÉTODO SEPARADO PARA ACTUALIZAR ---
    @Override
    public Categoria updateCategoria(Integer id, CategoriaDTO categoriaDTO) {
        // 1. Busca la categoría EXISTENTE por su ID
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        // 2. Verifica si el NUEVO nombre ya está en uso por OTRA categoría
        Optional<Categoria> categoriaConMismoNombre = categoriaRepository.findByNombreIgnoreCase(categoriaDTO.getNombre());
        
        // Compara el ID de la categoría encontrada con el ID de la que estamos actualizando
        if (categoriaConMismoNombre.isPresent() && !categoriaConMismoNombre.get().getId().equals(categoriaExistente.getId())) {
            throw new IllegalArgumentException("Ya existe otra categoría con el nombre: " + categoriaDTO.getNombre());
        }

        // 3. Actualiza los datos
        categoriaExistente.setNombre(categoriaDTO.getNombre());
        categoriaExistente.setDescripcion(categoriaDTO.getDescripcion());
        
        // 4. Guarda
        return categoriaRepository.save(categoriaExistente);
    }

    @Override
    @Transactional // Es buena práctica para asegurar consistencia si hay relaciones
    public void deleteCategoria(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        
        // Verifica si hay productos asociados ANTES de intentar borrar
        // Asegúrate de tener un método countByCategoriaId en ProductoRepository
        if (productoRepository.countByCategoriaId(id) > 0) { 
            throw new IllegalArgumentException("No se puede eliminar la categoría '" + categoria.getNombre() + "' porque tiene libros asociados.");
        }
        
        categoriaRepository.deleteById(id);
    }
}
