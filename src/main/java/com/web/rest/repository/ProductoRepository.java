package com.web.rest.repository;

import com.web.rest.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    Optional<Producto> findByIsbn(String isbn);

    List<Producto> findByTituloContainingIgnoreCase(String titulo);

    List<Producto> findByAutorContainingIgnoreCase(String autor);

    List<Producto> findByCategoria_Id(Integer categoriaId);

    List<Producto> findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase(String titulo, String autor);
    
    long countByCategoriaId(Integer categoriaId); 
}