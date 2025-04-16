package com.carlosdourado.translatorapi.domain.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String subject;
    private String content;
    private String locale;

    private String authorEmail;

    private String sourceLanguage;
    private String targetLanguage;

    @ManyToOne
    @JoinColumn(name = "translator_id")
    private Translator translator;
}
