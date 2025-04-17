package com.carlosdourado.translatorapi.application.dtos.translationTask;

import com.carlosdourado.translatorapi.domain.entities.enums.TranslationTaskStatusEnum;

import java.util.UUID;

public record TranslationTaskResponse(
        UUID id,
        TranslationTaskStatusEnum status,
        UUID documentId,
        String errorMessage,
        boolean isCompleted
) {
}
