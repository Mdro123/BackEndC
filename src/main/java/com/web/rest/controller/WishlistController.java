package com.web.rest.controller;

import com.web.rest.dto.WishlistItemResponseDTO;
import com.web.rest.model.Usuario;
import com.web.rest.model.WishlistItem;
import com.web.rest.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "http://localhost:4200") // Asegúrate que coincida con tu frontend
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // Método auxiliar para obtener el ID del usuario logueado
    private Integer getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return ((Usuario) authentication.getPrincipal()).getId();
        }
        throw new IllegalStateException("Usuario no autenticado.");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<List<WishlistItemResponseDTO>> getMyWishlist() {
        Integer userId = getAuthenticatedUserId();
        List<WishlistItem> items = wishlistService.getWishlistByUserId(userId);
        
        // Convertimos la lista de entidades a DTOs
        List<WishlistItemResponseDTO> response = items.stream().map(item -> {
            WishlistItemResponseDTO dto = new WishlistItemResponseDTO();
            dto.setId(item.getId());
            dto.setProductoId(item.getProducto().getId());
            dto.setTitulo(item.getProducto().getTitulo());
            dto.setAutor(item.getProducto().getAutor());
            dto.setPrecio(item.getProducto().getPrecio());
            dto.setImagenUrl(item.getProducto().getImagenUrl());
            dto.setAddedAt(item.getAddedAt());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add/{productId}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<String> addToWishlist(@PathVariable Integer productId) {
        Integer userId = getAuthenticatedUserId();
        wishlistService.addToWishlist(userId, productId);
        return ResponseEntity.ok("Producto añadido a tu lista de deseos.");
    }

    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<String> removeFromWishlist(@PathVariable Integer productId) {
        Integer userId = getAuthenticatedUserId();
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok("Producto eliminado de tu lista de deseos.");
    }
}
