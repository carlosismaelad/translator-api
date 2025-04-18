package com.carlosdourado.translatorapi.application.dtos.shared;

import lombok.Getter;

import java.time.LocalDateTime;


public record ErrorResponse(int status, String message, LocalDateTime timestamp) {
}
