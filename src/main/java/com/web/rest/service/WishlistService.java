package com.web.rest.service;

import com.web.rest.model.WishlistItem;
import java.util.List;


public interface WishlistService {
    WishlistItem addToWishlist(Integer userId, Integer productoId);
    void removeFromWishlist(Integer userId, Integer productoId);
    List<WishlistItem> getWishlistByUserId(Integer userId);
}
