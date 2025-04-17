package com.carlosdourado.translatorapi.application.services.document;

import com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs.DocumentTranslationRequest;
import com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs.DocumentTranslationResponse;
import com.carlosdourado.translatorapi.application.dtos.translationTask.TranslationTaskResponse;
import com.carlosdourado.translatorapi.application.exceptions.DocumentNotFoundException;
import com.carlosdourado.translatorapi.application.services.openAi.OpenAiTranslationService;
import com.carlosdourado.translatorapi.application.services.translationTask.TranslationTaskService;
import com.carlosdourado.translatorapi.domain.entities.Document;
import com.carlosdourado.translatorapi.domain.entities.TranslationTask;
import com.carlosdourado.translatorapi.domain.entities.Translator;
import com.carlosdourado.translatorapi.domain.entities.enums.TranslationTaskStatusEnum;
import com.carlosdourado.translatorapi.domain.repositories.DocumentRepository;
import com.carlosdourado.translatorapi.domain.repositories.TranslationTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    @Autowired
    private TranslationTaskRepository translationTaskRepository;

    @Autowired
    private OpenAiTranslationService translationService;

    @Autowired
    private TranslationTaskService translationTaskService;

    public TranslationTaskResponse translateAndSave(DocumentTranslationRequest doc, Translator translator){
        String detectedSourceLanguage = doc.sourceLanguage() != null && !doc.sourceLanguage().isBlank()
                ? doc.sourceLanguage()
                : translationService.detectLocale(doc.content());

        Document document = new Document();
        document.setSubject(doc.subject());
        document.setContent(doc.content());
        document.setAuthorEmail(doc.authorEmail());
        document.setSourceLanguage(detectedSourceLanguage);
        document.setTargetLanguage(doc.targetLanguage());
        document.setTranslator(translator);

        Document savedDocument = repository.save(document);

        TranslationTask task = translationTaskService.createAndStartTask(savedDocument.getId());

        return new TranslationTaskResponse(
                task.getId(),
                task.getStatus(),
                savedDocument.getId(),
                null,
                null
        );
    }

    public List<DocumentTranslationResponse> getAllDocumentsByTranslator(UUID translatorId) {
        List<Document> documents = repository.findAllByTranslator(translatorId);
        return documents.stream()
                .map(this::toResponse)
                .toList();
    }

    public DocumentTranslationResponse getDocumentById(UUID documentId, UUID translatorId) {
        Document document = repository.findByIdAndTranslator(documentId, translatorId)
                .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado"));
        return toResponse(document);
    }

    private DocumentTranslationResponse toResponse(Document doc) {
        return new DocumentTranslationResponse(
                doc.getId(),
                doc.getSubject(),
                doc.getContent(),
                doc.getTranslatedContent(),
                doc.getAuthorEmail(),
                doc.getSourceLanguage(),
                doc.getTargetLanguage(),
                doc.getTranslator().getId()
        );
    }
}
