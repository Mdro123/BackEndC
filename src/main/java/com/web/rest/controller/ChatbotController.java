package com.web.rest.controller;

import com.web.rest.dto.ChatRequestDTO;
import com.web.rest.dto.ChatResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate; 
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:4200/") // Permite peticiones desde tu frontend
public class ChatbotController {

    // Inyecta la URL de la API de Gemini desde application.properties
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    // RestTemplate es una forma sencilla de hacer llamadas a otras APIs
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/ask")
    public ResponseEntity<ChatResponseDTO> askGemini(@Valid @RequestBody ChatRequestDTO request) {
        
        // 1. Preparar la petición para la API de Gemini
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Estructura del cuerpo según la API de Gemini (simplificada)
        // Puedes ajustar el "prompt" aquí para darle más contexto al chatbot
        Map<String, Object> body = new HashMap<>();
        Map<String, String> textPart = new HashMap<>();
        textPart.put("text", "Eres un asistente virtual de la Librería Crisol. Responde de forma breve y amigable. Pregunta del usuario: " + request.getMessage());
        Map<String, List<Map<String, String>>> content = new HashMap<>();
        content.put("parts", Collections.singletonList(textPart));
        body.put("contents", Collections.singletonList(content));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // 2. Llamar a la API de Gemini
            ResponseEntity<Map> response = restTemplate.exchange(
                    geminiApiUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class // Esperamos una respuesta genérica tipo Map
            );

            // 3. Extraer la respuesta de Gemini del JSON complejo que devuelve
            //    NOTA: La estructura exacta de la respuesta puede cambiar, revisa la documentación de Gemini.
            //    Este es un ejemplo basado en la estructura común.
            String reply = "Lo siento, no pude procesar la respuesta."; // Mensaje por defecto
            if (response.getBody() != null && response.getBody().containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
                if (!candidates.isEmpty() && candidates.get(0).containsKey("content")) {
                    Map<String, Object> contentResponse = (Map<String, Object>) candidates.get(0).get("content");
                    if (contentResponse.containsKey("parts")) {
                        List<Map<String, String>> parts = (List<Map<String, String>>) contentResponse.get("parts");
                        if (!parts.isEmpty() && parts.get(0).containsKey("text")) {
                            reply = parts.get(0).get("text");
                        }
                    }
                }
            }
            
            return ResponseEntity.ok(new ChatResponseDTO(reply));

        } catch (Exception e) {
            // Manejo básico de errores
            System.err.println("Error llamando a la API de Gemini: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al comunicarse con el servicio de IA.", e);
        }
    }
}
