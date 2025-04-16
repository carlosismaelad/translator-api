package com.carlosdourado.translatorapi.domain.repositories;

import com.carlosdourado.translatorapi.domain.entities.TranslationProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TranslationProfileRepository extends JpaRepository<TranslationProfile, UUID> {
    Optional<TranslationProfile> findByTranslatorEmail(String email);
}
