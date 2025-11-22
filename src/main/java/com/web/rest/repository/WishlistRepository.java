package com.web.rest.repository;

import com.web.rest.model.Producto;
import com.web.rest.model.Usuario;
import com.web.rest.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Integer> {
    // Verifica si ya existe el libro en la lista del usuario
    boolean existsByUsuarioAndProducto(Usuario usuario, Producto producto);
    
    // Borra un libro específico de la lista de un usuario
    void deleteByUsuarioAndProducto(Usuario usuario, Producto producto);
    
    // Obtiene toda la lista de deseos de un usuario
    List<WishlistItem> findByUsuario(Usuario usuario);
    
}
