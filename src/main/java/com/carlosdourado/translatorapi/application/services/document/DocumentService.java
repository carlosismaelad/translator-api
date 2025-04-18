package com.carlosdourado.translatorapi.application.services.document;

import com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs.DocumentTranslationRequest;
import com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs.DocumentTranslationResponse;
import com.carlosdourado.translatorapi.application.dtos.translationTask.TranslationTaskResponse;
import com.carlosdourado.translatorapi.application.exceptions.BlankOrNullParametersException;
import com.carlosdourado.translatorapi.application.exceptions.DocumentNotFoundException;
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
    private TranslationTaskService translationTaskService;

    public TranslationTaskResponse translateAndSave(DocumentTranslationRequest doc, Translator translator){

        if(doc.subject() == null || doc.subject().isBlank())
            throw new BlankOrNullParametersException("O campo assunto do documento não pode estar em branco.");

        if(doc.authorEmail() == null || doc.authorEmail().isBlank())
            throw new BlankOrNullParametersException("O campo email do autor não pode estar em branco.");

        if (doc.targetLanguage() == null || doc.targetLanguage().isBlank())
            throw new BlankOrNullParametersException("A campo 'língua de destino' não pode ser estar em branco.");

        if (doc.content() == null || doc.content().isBlank())
            throw new BlankOrNullParametersException("O conteúdo não pode ser estar em branco.");

        Document document = new Document();
        document.setSubject(doc.subject());
        document.setContent(doc.content());
        document.setAuthorEmail(doc.authorEmail());
        document.setSourceLanguage(doc.sourceLanguage());
        document.setTargetLanguage(doc.targetLanguage());
        document.setTranslator(translator);

        Document savedDocument = repository.save(document);

        TranslationTask task = translationTaskService.createAndStartTask(savedDocument.getId());

        return new TranslationTaskResponse(
                task.getId(),
                task.getStatus(),
                savedDocument.getId(),
                task.getErrorMessage(),
                TranslationTaskStatusEnum.COMPLETED.equals(task.getStatus())
        );
    }

    public List<DocumentTranslationResponse> getAllDocumentsByTranslator(UUID translatorId) {
        List<Document> documents = repository.findAllByTranslator_Id(translatorId);
        return documents.stream()
                .map(this::toResponse)
                .toList();
    }

    public DocumentTranslationResponse getDocumentById(UUID documentId, UUID translatorId) {
        Document document = repository.findByIdAndTranslator_Id(documentId, translatorId)
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
