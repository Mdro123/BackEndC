package com.web.rest.controller;

import com.web.rest.dto.ProductoDTO;
import com.web.rest.dto.ProductoResponseDTO;
import com.web.rest.dto.ProductoMasVendidoDTO;
import com.web.rest.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@GetMapping
	public ResponseEntity<List<ProductoResponseDTO>> getAllProductos() {
		List<ProductoResponseDTO> productos = productoService.getAllProductos();
		return ResponseEntity.ok(productos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductoResponseDTO> getProductoById(@PathVariable Integer id) {
		ProductoResponseDTO producto = productoService.getProductoById(id);
		return ResponseEntity.ok(producto);
	}

	@GetMapping("/buscar")
	public ResponseEntity<List<ProductoResponseDTO>> buscarProductos(@RequestParam String termino) {
		List<ProductoResponseDTO> productos = productoService.buscarProductos(termino);
		return ResponseEntity.ok(productos);
	}

	@GetMapping("/categoria/{idCategoria}")
	public ResponseEntity<List<ProductoResponseDTO>> getProductosByCategoria(@PathVariable Integer idCategoria) {
		List<ProductoResponseDTO> productos = productoService.getProductosByCategoria(idCategoria);
		return ResponseEntity.ok(productos);
	}

	@GetMapping("/mas-vendidos")
	public ResponseEntity<List<ProductoMasVendidoDTO>> getTopSellingProducts(
			@RequestParam(defaultValue = "10") int limit) {
		List<ProductoMasVendidoDTO> topProducts = productoService.getTopSellingProducts(limit);
		return ResponseEntity.ok(topProducts);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductoResponseDTO> saveProducto(@Valid @RequestBody ProductoDTO productoDTO) {
		ProductoResponseDTO result = productoService.saveProducto(productoDTO);
		HttpStatus status = (productoDTO.getId() == null) ? HttpStatus.CREATED : HttpStatus.OK;
		return new ResponseEntity<>(result, status);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
		productoService.deleteProducto(id);
		return ResponseEntity.noContent().build();
	}
}