package com.carlosdourado.translatorapi.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class TranslationProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String sourceLanguage;
    private String targetLanguage;

    @ManyToOne
    @JoinColumn(name = "translator_id")
    private Translator translator;
}
