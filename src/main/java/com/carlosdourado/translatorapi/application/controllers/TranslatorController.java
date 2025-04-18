package com.carlosdourado.translatorapi.application.controllers;

import com.carlosdourado.translatorapi.application.dtos.translatorDTOs.TranslatorRequest;
import com.carlosdourado.translatorapi.application.dtos.translatorDTOs.TranslatorResponse;
import com.carlosdourado.translatorapi.application.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/translator")
@PreAuthorize("isAuthenticated()")
public class TranslatorController {
    @Autowired
    private TranslatorService translatorService;

    @PostMapping
    public ResponseEntity<TranslatorResponse> create(@RequestBody TranslatorRequest request) {
            return ResponseEntity.ok(translatorService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TranslatorResponse> update(@PathVariable UUID id, @RequestBody TranslatorRequest request) {
        return ResponseEntity.ok(translatorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        translatorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TranslatorResponse>> findAll() {
        return ResponseEntity.ok(translatorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TranslatorResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(translatorService.findById(id));
    }
}
