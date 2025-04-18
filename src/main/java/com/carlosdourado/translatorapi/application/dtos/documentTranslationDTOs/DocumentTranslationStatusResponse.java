package com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs;

import java.util.UUID;

public record DocumentTranslationStatusResponse(
        UUID id,
        String status,
        boolean isReady
) {
}
