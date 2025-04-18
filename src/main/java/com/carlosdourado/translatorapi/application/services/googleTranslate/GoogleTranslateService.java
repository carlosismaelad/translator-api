package com.carlosdourado.translatorapi.application.services.googleTranslate;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class GoogleTranslateService {

    private final Translate translate;

    private static final String GOOGLE_CREDENTIALS_PATH = "/home/carlos-dourado/www/java-projects/translator-api/credentials.json";

    public GoogleTranslateService() {
        try {
            File credentialsPath = new File(GOOGLE_CREDENTIALS_PATH);
            if (credentialsPath.exists()) {
                GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setCredentials(credentials)
                        .build();
                this.translate = options.getService();
            } else {
                throw new RuntimeException("Credenciais não encontradas: " + GOOGLE_CREDENTIALS_PATH);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar as credenciais do Google: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao criar o GoogleTranslateService: " + e.getMessage(), e);
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

