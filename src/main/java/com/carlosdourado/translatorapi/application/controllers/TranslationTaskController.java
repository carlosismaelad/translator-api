package com.carlosdourado.translatorapi.application.controllers;

import com.carlosdourado.translatorapi.application.dtos.translationTask.TranslationTaskResponse;
import com.carlosdourado.translatorapi.application.services.translationTask.TranslationTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/translation-task")
@PreAuthorize("isAuthenticated()")
public class TranslationTaskController {
    private final TranslationTaskService translationTaskService;

    public TranslationTaskController(TranslationTaskService translationTaskService) {
        this.translationTaskService = translationTaskService;
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TranslationTaskResponse> getTaskById(@PathVariable UUID taskId) {
        return ResponseEntity.ok(translationTaskService.getTask(taskId));
    }

    @GetMapping("/{taskId}/status")
    public ResponseEntity<Map<String, Object>> getTaskStatus(@PathVariable UUID taskId) {
        return ResponseEntity.ok(translationTaskService.getTaskStatus(taskId));
    }
}
