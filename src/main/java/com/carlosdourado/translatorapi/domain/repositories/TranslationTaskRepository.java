package com.carlosdourado.translatorapi.domain.repositories;

import com.carlosdourado.translatorapi.domain.entities.TranslationTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TranslationTaskRepository extends JpaRepository<TranslationTask, UUID> {
    List<TranslationTask> findAllByDocument_Translator_Id(UUID translatorId);
    Optional<TranslationTask> findByIdAndDocument_Translator_Id(UUID id, UUID translatorId);
}
