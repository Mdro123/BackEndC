package com.web.rest.service.impl;

import com.web.rest.dto.CategoriaDTO;
import com.web.rest.exception.ResourceNotFoundException;
import com.web.rest.model.Categoria;
import com.web.rest.repository.CategoriaRepository;
import com.web.rest.repository.ProductoRepository;
import com.web.rest.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

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

    @Override
    public Categoria saveCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria;
        boolean isUpdate = categoriaDTO.getId() != null;

        if (isUpdate) {
            categoria = categoriaRepository.findById(categoriaDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + categoriaDTO.getId()));

            if (!categoria.getNombre().equalsIgnoreCase(categoriaDTO.getNombre())) {
                Optional<Categoria> categoriaConMismoNombre = categoriaRepository.findByNombreIgnoreCase(categoriaDTO.getNombre());
                
                // Compara con el ID de la entidad existente
                if (categoriaConMismoNombre.isPresent() && !categoriaConMismoNombre.get().getId().equals(categoria.getId())) {
                    throw new IllegalArgumentException("Ya existe otra categoría con el nombre: " + categoriaDTO.getNombre());
                }
            }
        } else {
            if (categoriaRepository.findByNombreIgnoreCase(categoriaDTO.getNombre()).isPresent()) {
                throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
            }
            categoria = new Categoria();
        }

        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());

        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public void deleteCategoria(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        
        // Asegúrate de tener este método en ProductoRepository: long countByCategoriaId(Integer id);
        if (productoRepository.countByCategoriaId(id) > 0) { 
            throw new IllegalArgumentException("No se puede eliminar la categoría '" + categoria.getNombre() + "' porque tiene libros asociados.");
        }
        
        categoriaRepository.deleteById(id);
    }
}
