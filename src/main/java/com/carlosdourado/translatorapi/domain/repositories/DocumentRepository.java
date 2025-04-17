package com.carlosdourado.translatorapi.domain.repositories;

import com.carlosdourado.translatorapi.domain.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findAllByTranslator_Id(UUID translatorId);

    Optional<Document> findByIdAndTranslator_Id(UUID documentId, UUID translatorId);
}
