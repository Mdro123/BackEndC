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

// DTOs definidos como clases anidadas estáticas para simplicidad
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

// Estructura del JSON para la API de Gemini
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

// Estructura de la respuesta JSON de Gemini (simplificada)
class GeminiResponse {
    public List<Candidate> candidates;
    static class Candidate {
        public Content content;
    }
    static class Content {
        public List<Part> parts;
    }
    static class Part {
        public String text;
    }
    public String getReplyText() {
        try {
            return this.candidates.get(0).content.parts.get(0).text;
        } catch (Exception e) {
            return null; // Devuelve null si la estructura no es la esperada
        }
    }
}


@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:4200/")
public class ChatbotController {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/ask")
    public ResponseEntity<ChatResponseDTO> askGemini(@Valid @RequestBody ChatRequestDTO request) {
        
        // 1. Preparar la petición para la API de Gemini
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Creamos el prompt (contexto)
        String prompt = "Eres un asistente virtual de la Librería Crisol. Responde de forma breve y amigable. Pregunta del usuario: " + request.getMessage();
        
        // Usamos las clases POJO para crear el cuerpo de la solicitud (más seguro que Map)
        GeminiRequest geminiRequest = new GeminiRequest(prompt);
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(geminiRequest, headers);

        try {
            // 2. Llamar a la API de Gemini
            ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                    geminiApiUrl,
                    HttpMethod.POST,
                    entity,
                    GeminiResponse.class // Mapea la respuesta directamente a nuestra clase POJO
            );

            // 3. Extraer la respuesta de forma segura
            String reply = "Lo siento, no pude procesar la respuesta."; // Mensaje por defecto
            if (response.getBody() != null && response.getBody().getReplyText() != null) {
                reply = response.getBody().getReplyText();
            }
            
            return ResponseEntity.ok(new ChatResponseDTO(reply));

        } catch (Exception e) {
            // Manejo de errores
            System.err.println("Error llamando a la API de Gemini: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al comunicarse con el servicio de IA.", e);
        }
    }
}
