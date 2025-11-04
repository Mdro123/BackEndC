package com.web.rest.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder; // Importa RestTemplateBuilder
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// --- DTOs y Clases para Gemini (sin cambios) ---
class ChatRequestDTO {
    @NotBlank
    private String message;
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
class ChatResponseDTO {
    private String reply;
    public ChatResponseDTO(String reply) { this.reply = reply; }
    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
}
class GeminiRequest { /* ... (código sin cambios) ... */ }
class GeminiResponse { /* ... (código sin cambios) ... */ }
// ----------------------------------------------------

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:4200/")
public class ChatbotController {

    @Value("${gemini.api.baseurl}")
    private String geminiApiBaseUrl;

    @Value("${google.ai.apiKey}")
    private String geminiApiKey;

    private final RestTemplate restTemplate;

    // --- Constructor modificado ---
    public ChatbotController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build(); // Deja que Spring construya el RestTemplate
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatResponseDTO> askGemini(@Valid @RequestBody ChatRequestDTO request) {
        
        // --- LOG DE DEBUGGING ---
        System.out.println("ChatbotController: Método askGemini iniciado.");
        if (geminiApiBaseUrl == null || geminiApiBaseUrl.isEmpty()) {
            System.err.println("¡ERROR FATAL: gemini.api.baseurl no está inyectado!");
        }
        if (geminiApiKey == null || geminiApiKey.isEmpty()) {
            System.err.println("¡ERROR FATAL: google.ai.apiKey no está inyectado!");
        }
        // -------------------------

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = "Eres un asistente virtual de la Librería Crisol. Responde de forma breve y amigable. Pregunta del usuario: " + request.getMessage();
        GeminiRequest geminiRequest = new GeminiRequest(prompt);
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(geminiRequest, headers);

        String apiUrl = geminiApiBaseUrl + "?key=" + geminiApiKey;

        // --- LOG DE DEBUGGING ---
        // Ocultamos la clave para no exponerla en los logs
        System.out.println("Llamando a la URL de Gemini: " + geminiApiBaseUrl + "?key=...OCULTA...");
        // -------------------------

        try {
            ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    GeminiResponse.class
            );

            String reply = "Lo siento, no pude procesar la respuesta.";
            if (response.getBody() != null && response.getBody().getReplyText() != null) {
                reply = response.getBody().getReplyText();
            }
            
            System.out.println("Respuesta de Gemini obtenida con éxito.");
            return ResponseEntity.ok(new ChatResponseDTO(reply));

        } catch (Exception e) {
            System.err.println("--- ERROR LLAMANDO A GEMINI API ---");
            System.err.println("Mensaje de Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("--- FIN DEL ERROR ---");
            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al comunicarse con el servicio de IA.", e);
        }
    }
}
