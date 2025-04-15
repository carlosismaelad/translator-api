package com.carlosdourado.translatorapi.application.dtos.translatorDTOs;

public record TranslatorRequest(String name, String email, String password, String sourceLanguage, String targetLanguage) {
}
