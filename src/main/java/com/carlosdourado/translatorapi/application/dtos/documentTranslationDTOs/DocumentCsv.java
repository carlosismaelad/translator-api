package com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs;

public record DocumentCsv(
        String subject,
        String content,
        String locale,
        String authorEmail
) {
}
