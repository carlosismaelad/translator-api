package com.carlosdourado.translatorapi.application.services.openAi;

import com.carlosdourado.translatorapi.application.exceptions.CommunicationFailureWithOpenAiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiTranslationService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate =  new RestTemplate();
    private final static String CHAT_COMPLETIO_URL = "https://api.openai.com/v1/chat/completions";


    public String detectLocale(String text) {
        String prompt = String.format("Detect the language of the following text and only the language name (e.g: English, Portuguese, Spanish): \n\n%s", text);
        return sendChatRequest(text);
    }

    public String translate(String text, String sourceLangueg, String targetLangue){
        String prompt = String.format(
                "Translate the following text from %s to %s:\n\n%s",
                sourceLangueg, targetLangue, text
        );
        return sendChatRequest(prompt);
    }

    private String sendChatRequest(String prompt){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = Map.of(
                "model", "gpt-3.5-turbo",
                "message", new Object[]{
                        Map.of("role", "user", "content", prompt)
                },
                "temperature", 0.7
        );
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                CHAT_COMPLETIO_URL,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Object rawChoices = response.getBody().get("choices");

            if (rawChoices instanceof List<?> choices && !choices.isEmpty()) {
                Object choice = choices.getFirst();
                if (choice instanceof Map<?, ?> choiceMap) {
                    Object messageObj = choiceMap.get("message");
                    if (messageObj instanceof Map<?, ?> messageMap) {
                        return (String) messageMap.get("content");
                    }
                }
            }
        }
        throw new CommunicationFailureWithOpenAiException("Failed to communicate with OpenAI API.");
    }
}
