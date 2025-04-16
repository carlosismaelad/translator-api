package com.carlosdourado.translatorapi.application.controllers;

import com.carlosdourado.translatorapi.application.dtos.translatioProfileDTOs.TranslationProfileRequest;
import com.carlosdourado.translatorapi.application.dtos.translatioProfileDTOs.TranslationProfileResponse;
import com.carlosdourado.translatorapi.application.services.translatorProfile.TranslationProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/translators/{translatorId}/profiles")
public class TranslationProfileController {

    @Autowired
    private final TranslationProfileService service;

    public TranslationProfileController(TranslationProfileService service) {
        this.service = service;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TranslationProfileResponse> create(
            @PathVariable UUID translatorId,
            @RequestBody TranslationProfileRequest request) {
        TranslationProfileResponse response = service.create(translatorId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<TranslationProfileResponse>> list(
            @PathVariable UUID translatorId) {
        List<TranslationProfileResponse> profiles = service.listByTranslator(translatorId);
        return ResponseEntity.ok(profiles);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{profileId}")
    public ResponseEntity<TranslationProfileResponse> update(
            @PathVariable UUID translatorId,
            @PathVariable UUID profileId,
            @RequestBody TranslationProfileRequest request) {
        TranslationProfileResponse updated = service.update(translatorId, profileId, request);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID translatorId,
            @PathVariable UUID profileId) {
        service.delete(translatorId, profileId);
        return ResponseEntity.noContent().build();
    }
}
