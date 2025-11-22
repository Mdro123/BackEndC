package com.web.rest.service.impl;

import com.web.rest.exception.ResourceNotFoundException;
import com.web.rest.model.Producto;
import com.web.rest.model.Usuario;
import com.web.rest.model.WishlistItem;
import com.web.rest.repository.ProductoRepository;
import com.web.rest.repository.UsuarioRepository;
import com.web.rest.repository.WishlistRepository;
import com.web.rest.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired private WishlistRepository wishlistRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProductoRepository productoRepository;

    @Override
    @Transactional
    public WishlistItem addToWishlist(Integer userId, Integer productoId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (wishlistRepository.existsByUsuarioAndProducto(usuario, producto)) {
            throw new IllegalArgumentException("El libro ya está en tu lista de deseos.");
        }

        WishlistItem newItem = new WishlistItem();
        newItem.setUsuario(usuario);
        newItem.setProducto(producto);
        return wishlistRepository.save(newItem);
    }

    @Override
    @Transactional
    public void removeFromWishlist(Integer userId, Integer productoId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        wishlistRepository.deleteByUsuarioAndProducto(usuario, producto);
    }

    @Override
    public List<WishlistItem> getWishlistByUserId(Integer userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return wishlistRepository.findByUsuario(usuario);
    }
}
