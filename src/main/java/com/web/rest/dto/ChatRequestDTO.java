package com.web.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatRequestDTO {
    @NotBlank(message = "El mensaje no puede estar vacío.")
    private String message;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

}
