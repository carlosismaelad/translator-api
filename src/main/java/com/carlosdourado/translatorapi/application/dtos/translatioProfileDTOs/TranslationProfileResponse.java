package com.carlosdourado.translatorapi.application.dtos.translatioProfileDTOs;

import java.util.UUID;

public record TranslationProfileResponse(UUID id, String sourceLanguage, String targetLangue, UUID translatorId) {
}
