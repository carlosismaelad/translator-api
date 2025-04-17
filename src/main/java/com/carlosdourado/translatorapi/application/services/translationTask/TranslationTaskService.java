package com.carlosdourado.translatorapi.application.services.translationTask;

import com.carlosdourado.translatorapi.application.dtos.translationTask.TranslationTaskResponse;
import com.carlosdourado.translatorapi.application.exceptions.TaskNotFoundException;
import com.carlosdourado.translatorapi.application.services.openAi.OpenAiTranslationService;
import com.carlosdourado.translatorapi.domain.entities.Document;
import com.carlosdourado.translatorapi.domain.entities.TranslationTask;
import com.carlosdourado.translatorapi.domain.entities.enums.TranslationTaskStatusEnum;
import com.carlosdourado.translatorapi.domain.repositories.DocumentRepository;
import com.carlosdourado.translatorapi.domain.repositories.TranslationTaskRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class TranslationTaskService {

    private final TranslationTaskRepository translationTaskRepository;
    private final DocumentRepository documentRepository;
    private final OpenAiTranslationService openAiTranslationService;

    public TranslationTaskService(TranslationTaskRepository translationTaskRepository, DocumentRepository documentRepository, OpenAiTranslationService openAiTranslationService) {
        this.translationTaskRepository = translationTaskRepository;
        this.documentRepository = documentRepository;
        this.openAiTranslationService = openAiTranslationService;
    }

    @Async
    public CompletableFuture<Void> processTranslationAsync(UUID taskId) {
        TranslationTask task = translationTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        try {
            task.setStatus(TranslationTaskStatusEnum.PROCESSING);
            translationTaskRepository.save(task);

            String translation = openAiTranslationService.translate(
                    task.getDocument().getContent(),
                    task.getDocument().getSourceLanguage(),
                    task.getDocument().getTargetLanguage()
            );

            task.setTranslatedContent(translation);
            task.setStatus(TranslationTaskStatusEnum.COMPLETED);
        } catch (Exception e) {
            task.setStatus(TranslationTaskStatusEnum.FAILED);
            task.setErrorMessage(e.getMessage());
        }
        translationTaskRepository.save(task);
        return CompletableFuture.completedFuture(null);
    }

    public TranslationTask createAndStartTask(UUID documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        TranslationTask task = new TranslationTask();
        task.setDocument(document);
        task.setStatus(TranslationTaskStatusEnum.PENDING);
        task = translationTaskRepository.save(task);

        processTranslationAsync(task.getId());

        return task;
    }

    public TranslationTaskResponse getTask(UUID taskId) {
        TranslationTask task = translationTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada"));

        return new TranslationTaskResponse(
                task.getId(),
                task.getStatus(),
                task.getDocument().getId(),
                task.getTranslatedContent(),
                task.getErrorMessage()
        );
    }

    public Map<String, Object> getTaskStatus(UUID taskId) {
        TranslationTask task = translationTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada"));

        return Map.of(
                "status", task.getStatus(),
                "isCompleted", "COMPLETED".equals(task.getStatus()),
                "isFailed", "FAILED".equals(task.getStatus())
        );
    }

    public TranslationTaskResponse getTaskById(UUID taskId, UUID translatorId) {
        TranslationTask task = translationTaskRepository.findByIdAndDocument_Translator_Id(taskId, translatorId)
                .orElseThrow(() -> new RuntimeException("Task not found or not accessible"));
        return toResponse(task);
    }

    public List<TranslationTaskResponse> getTasksByTranslator(UUID translatorId) {
        return translationTaskRepository.findAllByDocument_Translator_Id(translatorId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TranslationTaskResponse toResponse(TranslationTask task) {
        return new TranslationTaskResponse(
                task.getId(),
                task.getStatus(),
                task.getDocument().getId(),
                task.getTranslatedContent(),
                task.getErrorMessage()
        );
    }
}
