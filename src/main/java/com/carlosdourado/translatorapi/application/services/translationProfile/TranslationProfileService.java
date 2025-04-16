package com.carlosdourado.translatorapi.application.services.translationProfile;

import com.carlosdourado.translatorapi.application.dtos.translatioProfileDTOs.TranslationProfileRequest;
import com.carlosdourado.translatorapi.application.dtos.translatioProfileDTOs.TranslationProfileResponse;
import com.carlosdourado.translatorapi.application.exceptions.TranslationProfileNotFoundException;
import com.carlosdourado.translatorapi.application.exceptions.TranslatorNotFoundException;
import com.carlosdourado.translatorapi.domain.entities.TranslationProfile;
import com.carlosdourado.translatorapi.domain.entities.Translator;
import com.carlosdourado.translatorapi.domain.repositories.TranslationProfileRepository;
import com.carlosdourado.translatorapi.domain.repositories.TranslatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslationProfileService {
    private final TranslationProfileRepository profileRepository;
    private final TranslatorRepository translatorRepository;

    public TranslationProfileResponse create(UUID translatorId, TranslationProfileRequest request) {
        Translator translator = translatorRepository.findById(translatorId)
                .orElseThrow(() -> new TranslatorNotFoundException(translatorId));

        TranslationProfile profile = new TranslationProfile();
        profile.setSourceLanguage(request.sourceLanguage());
        profile.setTargetLanguage(request.targetLanguage());
        profile.setTranslator(translator);

        profile = profileRepository.save(profile);
        return map(profile);
    }

    public List<TranslationProfileResponse> listByTranslator(UUID translatorId) {
        Translator translator = translatorRepository.findById(translatorId)
                .orElseThrow(() -> new TranslatorNotFoundException(translatorId));

        return profileRepository.findByTranslatorId(translator.getId()).stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public TranslationProfileResponse update(UUID translatorId, UUID profileId, TranslationProfileRequest request) {
        translatorRepository.findById(translatorId)
                .orElseThrow(() -> new TranslatorNotFoundException(translatorId));

        TranslationProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new TranslationProfileNotFoundException("Perfil de tradução não encontrado."));

        if (!profile.getTranslator().getId().equals(translatorId)) {
            throw new TranslationProfileNotFoundException("Perfil não pertence ao tradutor informado.");
        }

        profile.setSourceLanguage(request.sourceLanguage());
        profile.setTargetLanguage(request.targetLanguage());

        profile = profileRepository.save(profile);
        return map(profile);
    }

    public void delete(UUID translatorId, UUID profileId) {
        translatorRepository.findById(translatorId)
                .orElseThrow(() -> new TranslatorNotFoundException(translatorId));

        TranslationProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new TranslationProfileNotFoundException("Perfil de tradução não encontrado."));

        if (!profile.getTranslator().getId().equals(translatorId)) {
            throw new TranslationProfileNotFoundException("Perfil não pertence ao tradutor informado.");
        }

        profileRepository.delete(profile);
    }

    private TranslationProfileResponse map(TranslationProfile profile) {
        return new TranslationProfileResponse(
                profile.getId(),
                profile.getSourceLanguage(),
                profile.getTargetLanguage(),
                profile.getTranslator().getId()
        );
    }
}
