package com.carlosdourado.translatorapi.application.services.document;

import com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs.DocumentCsv;
import com.carlosdourado.translatorapi.application.dtos.documentTranslationDTOs.TranslatorCsv;
import com.carlosdourado.translatorapi.application.exceptions.CsvReadException;
import com.carlosdourado.translatorapi.application.exceptions.TranslatorNotFoundException;
import com.carlosdourado.translatorapi.application.services.googleTranslate.GoogleTranslateService;
import com.carlosdourado.translatorapi.application.services.translationTask.TranslationTaskService;
import com.carlosdourado.translatorapi.domain.entities.Document;
import com.carlosdourado.translatorapi.domain.entities.Translator;
import com.carlosdourado.translatorapi.domain.repositories.DocumentRepository;
import com.carlosdourado.translatorapi.domain.repositories.TranslatorRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class DocumentCsvService {

    private final DocumentRepository documentRepository;
    private final TranslatorRepository translatorRepository;
    private final TranslationTaskService translationTaskService;
    private final GoogleTranslateService googleTranslateService;

    public DocumentCsvService(DocumentRepository documentRepository, TranslatorRepository translatorRepository, TranslationTaskService translationTaskService, GoogleTranslateService googleTranslateService) {
        this.documentRepository = documentRepository;
        this.translatorRepository = translatorRepository;
        this.translationTaskService = translationTaskService;
        this.googleTranslateService = googleTranslateService;
    }

    public ResponseEntity<Resource> processCsv(
            MultipartFile translatorsCsv,
            MultipartFile documentsCsv,
            Translator authenticatedTranslatorId
    ) {
        Map<String, TranslatorCsv> translatorMap = loadTranslators(translatorsCsv);

        Translator authenticatedTranslator = translatorRepository.findById(authenticatedTranslatorId.getId())
                .orElseThrow(() -> new TranslatorNotFoundException(authenticatedTranslatorId.getId()));

        List<Document> processedDocuments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(documentsCsv.getInputStream()))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");

                if (fields.length < 4)
                    throw new CsvReadException("CSV de tradutores não está no padrão adequado: name, email, souce_langue and target_language.");

                String subject = fields[0].trim();
                String content = fields[1].trim();
                String detectedLanguage = fields[2].trim();
                String translatorEmail = fields[3].trim();

                TranslatorCsv translatorCsv = translatorMap.get(translatorEmail);
                if (translatorCsv == null)
                    throw new CsvReadException("CSV de tradutores não pode estar vazio.");

                Document document = new Document();
                document.setSubject(subject);
                document.setContent(content);
                document.setSourceLanguage(translatorCsv.sourceLanguage());
                document.setAuthorEmail(translatorCsv.email());
                document.setTargetLanguage(translatorCsv.targetLanguage());
                document.setTranslator(authenticatedTranslator);

                processedDocuments.add(document);

                boolean isLarge = content.length() > 500;

                document = documentRepository.save(document);

                if (!isLarge) {
                    String translated = googleTranslateService.translate(
                            content,
                            translatorCsv.sourceLanguage(),
                            translatorCsv.targetLanguage()
                    );
                    document.setTranslatedContent(translated);
                    documentRepository.save(document);
                } else {
                    translationTaskService.createAndStartTask(document.getId());
                }
            }
        } catch (Exception e) {
            throw new CsvReadException("Houve um erro ao tentar fazer a leitura do CSV ou o documento não está no padrão esperado");
        }

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("title;original_content;translated_content;source_language;target_language;translator_email\n");

        for (Document doc : processedDocuments) {
            String translatedContent = doc.getTranslatedContent() != null ? doc.getTranslatedContent() : "";
            csvBuilder
                    .append(doc.getSubject()).append(";")
                    .append(doc.getContent().replace(";", ",")).append(";")
                    .append(translatedContent.replace(";", ",")).append(";")
                    .append(doc.getSourceLanguage()).append(";")
                    .append(doc.getTargetLanguage()).append(";")
                    .append(doc.getTranslator().getEmail()).append("\n");
        }

        String bom = "\uFEFF";
        String csvContent = bom + csvBuilder.toString();
        Resource resource = new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"translated_documents.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    private Map<String, TranslatorCsv> loadTranslators(MultipartFile csv) {
        Map<String, TranslatorCsv> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csv.getInputStream()))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length < 4) continue;

                String name = fields[0].trim();
                String email = fields[1].trim();
                String source = fields[2].trim();
                String target = fields[3].trim();

                map.put(email, new TranslatorCsv(name, email, source, target));
            }
        } catch (IOException e) {
            throw new CsvReadException("Erro ao processar CSV de tradutores");
        }
        return map;
    }
}
