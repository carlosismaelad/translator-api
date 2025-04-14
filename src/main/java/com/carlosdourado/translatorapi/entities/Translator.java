package com.carlosdourado.translatorapi.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Translator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String email;
    private String sourceLanguage;
    private String targetLanguage;

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL)
    private List<Document> documents;
}
