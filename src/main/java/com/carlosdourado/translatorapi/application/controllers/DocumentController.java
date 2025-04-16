package com.carlosdourado.translatorapi.application.controllers;

import com.carlosdourado.translatorapi.application.services.document.DocumentService;
import com.carlosdourado.translatorapi.domain.entities.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<Document> create(@RequestBody Document doc) {
        Document saved = documentService.save(doc);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        // lógica para ler CSV e criar documentos
        return ResponseEntity.ok().build();
    }
}
