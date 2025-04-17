package com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs;

import java.util.UUID;

public record DocumentTranslationResponse(
        UUID id,
        String subject,
        String content,
        String translatedContent,
        String authorEmail,
        String sourceLanguage,
        String targetLanguage,
        UUID translatorId
) {
}
