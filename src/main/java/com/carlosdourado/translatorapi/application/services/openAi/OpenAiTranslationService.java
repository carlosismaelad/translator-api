package com.carlosdourado.translatorapi.application.services.openAi;

import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    public String detectLocale(String content) {
        // Integração com OpenAI API para detectar idioma
        return "en-US";
    }
}
