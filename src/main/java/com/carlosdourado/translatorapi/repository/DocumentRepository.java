package com.carlosdourado.translatorapi.repository;

import com.carlosdourado.translatorapi.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> documents(String locale);
}
