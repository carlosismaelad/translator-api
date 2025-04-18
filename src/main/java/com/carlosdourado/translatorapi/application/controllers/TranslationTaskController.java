package com.carlosdourado.translatorapi.application.controllers;

import com.carlosdourado.translatorapi.application.dtos.translationTask.TranslationTaskResponse;
import com.carlosdourado.translatorapi.application.services.translationTask.TranslationTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/tasks")
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

    @GetMapping("/task/{taskId}/status")
    public ResponseEntity<Map<String, Object>> getTaskStatus(@PathVariable UUID taskId) {
        return ResponseEntity.ok(translationTaskService.getTaskStatus(taskId));
    }

    @GetMapping("/task/{taskId}/translator/{translatorId}")
    public ResponseEntity<TranslationTaskResponse> getTaskByIdAndTranslatorId(@PathVariable UUID taskId, @PathVariable UUID translatorId){
        return ResponseEntity.ok(translationTaskService.getTaskByIdAnsTranslatorId(taskId, translatorId));
    }

    @GetMapping("/translator/{translatorId}")
    public ResponseEntity<List<TranslationTaskResponse>> getTasksByTranslatorId(@PathVariable UUID translatorId){
        return ResponseEntity.ok(translationTaskService.getTasksByTranslatorId(translatorId));
    }

}
