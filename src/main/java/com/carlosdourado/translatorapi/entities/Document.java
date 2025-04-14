package com.carlosdourado.translatorapi.entities;

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

    @ManyToOne
    @JoinColumn(name = "translator_id")
    private Translator translator;
}
