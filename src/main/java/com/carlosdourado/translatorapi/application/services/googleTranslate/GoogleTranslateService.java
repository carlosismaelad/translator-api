package com.carlosdourado.translatorapi.application.services.googleTranslate;

import com.carlosdourado.translatorapi.application.exceptions.CredentialsNotFoundException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Service;


import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class GoogleTranslateService {

    private final Translate translate;

    public GoogleTranslateService() {
        try {

            String credentialsJson = System.getenv("GOOGLE_CREDENTIALS_JSON");

            if (credentialsJson == null || credentialsJson.isEmpty()) {
                throw new CredentialsNotFoundException("Variável de ambiente GOOGLE_CREDENTIALS_JSON não está definida!");
            }

            InputStream credentialsStream = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8));

            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
            this.translate = TranslateOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
        } catch (Exception e) {
            throw new CredentialsNotFoundException("Erro ao carregar as credenciais do Google Translate.");
        }
    }

    public String translate(String text, String sourceLang, String targetLang) {
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(sourceLang),
                Translate.TranslateOption.targetLanguage(targetLang)
        );
        return translation.getTranslatedText();
    }

    public String detectSourceLanguage(String text){
        return translate.detect(text).getLanguage();
    }
}

