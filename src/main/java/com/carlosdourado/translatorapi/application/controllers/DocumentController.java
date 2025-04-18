package com.carlosdourado.translatorapi.application.controllers;

import com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs.DocumentTranslationRequest;
import com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs.DocumentTranslationResponse;
import com.carlosdourado.translatorapi.application.dtos.translationTask.TranslationTaskResponse;
import com.carlosdourado.translatorapi.application.services.document.DocumentCsvService;
import com.carlosdourado.translatorapi.application.services.document.DocumentService;
import com.carlosdourado.translatorapi.domain.entities.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/document")
@PreAuthorize("isAuthenticated()")
public class DocumentController {

    @Autowired
    private final DocumentService documentService;

    @Autowired
    private DocumentCsvService documentCsvService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/translate")
    public ResponseEntity<TranslationTaskResponse> translateDocument(
            @RequestBody DocumentTranslationRequest request,
            @AuthenticationPrincipal Translator translator
    ) {
        TranslationTaskResponse taskResponse = documentService.translateAndSave(request, translator);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @GetMapping
    public ResponseEntity<List<DocumentTranslationResponse>> getDocuments(@AuthenticationPrincipal Translator translator) {
        return ResponseEntity.ok(documentService.getAllDocumentsByTranslator(translator.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentTranslationResponse> getDocumentById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Translator translator
    ) {
        return ResponseEntity.ok(documentService.getDocumentById(id, translator.getId()));
    }

    @PostMapping("/csv/upload")
    public ResponseEntity<Resource> uploadCsv(
            @RequestParam MultipartFile translatorsCsv,
            @RequestParam MultipartFile documentsCsv,
            @AuthenticationPrincipal Translator authenticatedTranslator
    ) {
        return documentCsvService.processCsv(translatorsCsv, documentsCsv, authenticatedTranslator);
    }
}
