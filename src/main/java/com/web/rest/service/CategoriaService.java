package com.web.rest.service;

import com.web.rest.dto.CategoriaDTO;
import com.web.rest.model.Categoria;

import java.util.List;

public interface CategoriaService {

    List<Categoria> getAllCategorias();
    Categoria getCategoriaById(Integer id);
    List<Categoria> getCategoriasByNombre(String nombre);

    Categoria saveCategoria(CategoriaDTO categoriaDTO);

    void deleteCategoria(Integer id);
}