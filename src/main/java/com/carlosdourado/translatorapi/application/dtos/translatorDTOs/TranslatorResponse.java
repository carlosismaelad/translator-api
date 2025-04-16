package com.carlosdourado.translatorapi.application.dtos.translatorDTOs;

import java.util.UUID;

public record TranslatorResponse(UUID id, String name, String email) {
}
