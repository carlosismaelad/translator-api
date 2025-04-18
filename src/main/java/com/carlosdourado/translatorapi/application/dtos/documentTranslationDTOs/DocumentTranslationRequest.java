package com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs;

public record DocumentTranslationRequest(
        String subject,
        String content,
        String authorEmail,
        String sourceLanguage,
        String targetLanguage
) {
}
