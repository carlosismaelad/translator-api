package com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs;

public record TranslatorCsv(
        String name,
        String email,
        String sourceLanguage,
        String targetLanguage
) {
}
