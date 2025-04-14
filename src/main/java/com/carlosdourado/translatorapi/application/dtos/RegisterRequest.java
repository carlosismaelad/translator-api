package com.carlosdourado.translatorapi.application.dtos;

public record RegisterRequest(String name, String email, String password, String confirmPassword,
                              String sourceLanguage, String targetLanguage) {
}
