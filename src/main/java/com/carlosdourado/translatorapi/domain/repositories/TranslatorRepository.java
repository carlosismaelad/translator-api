package com.carlosdourado.translatorapi.domain.repositories;

import com.carlosdourado.translatorapi.application.dtos.translatorDTOs.TranslatorResponse;
import com.carlosdourado.translatorapi.domain.entities.Translator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TranslatorRepository extends JpaRepository<Translator, UUID> {
    Optional<Translator> findByEmail(String email);
    List<TranslatorResponse> findAllByEmail(String email);
}
