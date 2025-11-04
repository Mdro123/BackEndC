package com.web.rest.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap; // Asegúrate de que HashMap esté importado

// --- DTOs y Clases para Gemini (definidas aquí mismo para simplicidad) ---

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

class GeminiRequest {
    public List<Content> contents;
    public GeminiRequest(String text) {
        this.contents = Collections.singletonList(new Content(text));
    }
    static class Content {
        public List<Part> parts;
        public Content(String text) {
            this.parts = Collections.singletonList(new Part(text));
        }
    }
    static class Part {
        public String text;
        public Part(String text) { this.text = text; }
    }
}

class GeminiResponse {
    public List<Candidate> candidates;
    static class Candidate { public Content content; }
    static class Content { public List<Part> parts; }
    static class Part { public String text; }
    public String getReplyText() {
        try {
            // Verifica que la respuesta no esté vacía
            if (this.candidates != null && !this.candidates.isEmpty() &&
                this.candidates.get(0).content != null &&
                this.candidates.get(0).content.parts != null && !this.candidates.get(0).content.parts.isEmpty()) {
                
                return this.candidates.get(0).content.parts.get(0).text;
            } else {
                return null; // Estructura inesperada
            }
        } catch (Exception e) {
            return null; // Devuelve null si hay cualquier error al navegar la estructura
        }
    }
}
// -----------------------------------------------------------------

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:4200/")
public class ChatbotController {

    @Value("${gemini.api.baseurl}")
    private String geminiApiBaseUrl;

    @Value("${google.ai.apiKey}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/ask")
    public ResponseEntity<ChatResponseDTO> askGemini(@Valid @RequestBody ChatRequestDTO request) {
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = "Eres un asistente virtual de la Librería Crisol. Responde de forma breve y amigable. Pregunta del usuario: " + request.getMessage();
        GeminiRequest geminiRequest = new GeminiRequest(prompt);
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(geminiRequest, headers);

        String apiUrl = geminiApiBaseUrl + "?key=" + geminiApiKey;

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
            
            return ResponseEntity.ok(new ChatResponseDTO(reply));

        } catch (Exception e) {
            // --- LOGGING DE ERROR MEJORADO ---
            // Esto imprimirá el error real en tus logs de Azure
            System.err.println("--- ERROR LLAMANDO A GEMINI API ---");
            System.err.println("URL Usada: " + apiUrl.replaceAll(geminiApiKey, "TU_API_KEY_OCULTA")); // Oculta la clave en el log
            System.err.println("Mensaje de Error: " + e.getMessage());
            e.printStackTrace(); // Imprime la traza completa del error
            System.err.println("--- FIN DEL ERROR ---");
            // ------------------------------------
            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al comunicarse con el servicio de IA.", e);
        }
    }
}
