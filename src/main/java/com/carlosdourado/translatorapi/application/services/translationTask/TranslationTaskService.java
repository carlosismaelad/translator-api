package com.carlosdourado.translatorapi.application.services.translationTask;

import com.carlosdourado.translatorapi.application.dtos.translationTask.TranslationTaskResponse;
import com.carlosdourado.translatorapi.application.exceptions.DocumentNotFoundException;
import com.carlosdourado.translatorapi.application.exceptions.TaskNotFoundException;
import com.carlosdourado.translatorapi.application.services.googleTranslate.GoogleTranslateService;
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
    private final GoogleTranslateService googleTranslateService;


    public TranslationTaskService(TranslationTaskRepository translationTaskRepository, DocumentRepository documentRepository, GoogleTranslateService googleTranslateService) {
        this.translationTaskRepository = translationTaskRepository;
        this.documentRepository = documentRepository;
        this.googleTranslateService = googleTranslateService;
    }

    @Async
    public CompletableFuture<Void> processTranslationAsync(UUID taskId) {
        TranslationTask task = getTaskOrThrow(taskId);
        try {
            startProcessing(task);

            Document document = task.getDocument();

            String translated = googleTranslateService.translate(
                    document.getContent(),
                    document.getSourceLanguage(),
                    document.getTargetLanguage()
            );

            document.setTranslatedContent(translated);
            documentRepository.save(document);

            completeTask(task);
        } catch (Exception ex) {
            String errorMessage = "Erro ao processar tradução: " + ex.getMessage();
            failTask(task, errorMessage);
        }
        return CompletableFuture.completedFuture(null);
    }

    private TranslationTask getTaskOrThrow(UUID taskId) {
        return translationTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    private void startProcessing(TranslationTask task) {
        task.setStatus(TranslationTaskStatusEnum.PROCESSING);
        translationTaskRepository.save(task);
    }


    private void completeTask(TranslationTask task) {
        task.setStatus(TranslationTaskStatusEnum.COMPLETED);
        translationTaskRepository.save(task);
    }

    private void failTask(TranslationTask task, String errorMessage) {
        task.setStatus(TranslationTaskStatusEnum.FAILED);
        task.setErrorMessage(errorMessage);
        translationTaskRepository.save(task);
    }

    public TranslationTask createAndStartTask(UUID documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

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
                task.getErrorMessage(),
                TranslationTaskStatusEnum.COMPLETED.equals(task.getStatus())
        );
    }

    public Map<String, Object> getTaskStatus(UUID taskId) {
        TranslationTask task = translationTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada"));

        return Map.of(
                "status", task.getStatus(),
                "isCompleted", TranslationTaskStatusEnum.COMPLETED.equals(task.getStatus()),
                "isFailed", TranslationTaskStatusEnum.FAILED.equals(task.getStatus())
        );
    }

    public TranslationTaskResponse getTaskById(UUID taskId, UUID translatorId) {
        TranslationTask task = translationTaskRepository.findByIdAndDocument_Translator_Id(taskId, translatorId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found or not accessible"));
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
                task.getErrorMessage(),
                TranslationTaskStatusEnum.COMPLETED.equals(task.getStatus())
        );
    }
}
