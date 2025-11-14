package com.web.rest.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    @Test
    void testProductoCaro_DebeSerElegibleParaEnvioGratis() {
        // --- 1. GIVEN (Dado) ---
        // Un producto con un precio alto (mayor a 100)
        Producto producto = new Producto();
        producto.setPrecio(new BigDecimal("150.00")); // S/ 150.00

        // --- 2. WHEN (Cuando) ---
        // Llamamos al método que aún no existe
        boolean esElegible = producto.isElegibleParaEnvioGratis();

        // --- 3. THEN (Entonces) ---
        // Verificamos que el resultado sea VERDADERO
        assertTrue(esElegible, "Un producto de S/ 150.00 debe tener envío gratis");
    }

    @Test
    void testProductoBarato_NO_DebeSerElegibleParaEnvioGratis() {
        // --- 1. GIVEN (Dado) ---
        // Un producto con un precio bajo (menor a 100)
        Producto producto = new Producto();
        producto.setPrecio(new BigDecimal("50.00")); // S/ 50.00

        // --- 2. WHEN (Cuando) ---
        // Llamamos al método que aún no existe
        boolean esElegible = producto.isElegibleParaEnvioGratis();

        // --- 3. THEN (Entonces) ---
        // Verificamos que el resultado sea FALSO
        assertFalse(esElegible, "Un producto de S/ 50.00 NO debe tener envío gratis");
    }

    @Test
    void testProductoEnElLimite_DebeSerElegibleParaEnvioGratis() {
        // --- 1. GIVEN (Dado) ---
        // Un producto con un precio exacto de 100
        Producto producto = new Producto();
        producto.setPrecio(new BigDecimal("100.00")); // S/ 100.00

        // --- 2. WHEN (Cuando) ---
        boolean esElegible = producto.isElegibleParaEnvioGratis();

        // --- 3. THEN (Entonces) ---
        // Verificamos que el resultado sea VERDADERO (asumiendo que S/ 100 es inclusivo)
        assertTrue(esElegible, "Un producto de S/ 100.00 debe tener envío gratis");
    }

    @Test
    void testProductoSinPrecio_NO_DebeSerElegibleParaEnvioGratis() {
        // --- 1. GIVEN (Dado) ---
        // Un producto sin precio (null)
        Producto producto = new Producto();
        producto.setPrecio(null);

        // --- 2. WHEN (Cuando) ---
        boolean esElegible = producto.isElegibleParaEnvioGratis();

        // --- 3. THEN (Entonces) ---
        // Verificamos que el resultado sea FALSO
        assertFalse(esElegible, "Un producto con precio null NO debe tener envío gratis");
    }
}
