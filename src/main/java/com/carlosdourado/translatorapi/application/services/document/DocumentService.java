package com.carlosdourado.translatorapi.application.services.document;

import com.carlosdourado.translatorapi.application.services.openAi.OpenAiTranslationService;
import com.carlosdourado.translatorapi.domain.entities.Document;
import com.carlosdourado.translatorapi.domain.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    @Autowired
    private OpenAiTranslationService openAiTranslationService;

    public Document save(Document doc){
        if(doc.getLocale() == null || doc.getLocale().isBlank()){
            String detectedLocale = openAiTranslationService.detectLocale(doc.getLocale());
            doc.setLocale(detectedLocale);
        }
        return repository.save(doc);
    }
}
