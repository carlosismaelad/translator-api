package com.carlosdourado.translatorapi.application.services.translator;

import com.carlosdourado.translatorapi.application.dtos.translatorDTOs.TranslatorRequest;
import com.carlosdourado.translatorapi.application.dtos.translatorDTOs.TranslatorResponse;
import com.carlosdourado.translatorapi.domain.entities.Translator;
import com.carlosdourado.translatorapi.domain.repositories.TranslatorRepository;
import com.carlosdourado.translatorapi.infra.security.password.SaltGenerator;
import com.carlosdourado.translatorapi.infra.security.password.TranslatorPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TranslatorService {
    @Autowired
    private TranslatorRepository repository;

    public TranslatorResponse create(TranslatorRequest request) {
        if (repository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        String salt = SaltGenerator.saltGenerator();
        String hashedPassword = TranslatorPasswordEncoder.encode(request.password(), salt);

        Translator translator = new Translator();
        translator.setName(request.name());
        translator.setEmail(request.email());
        translator.setPassword(hashedPassword);
        translator.setSalt(salt);

        repository.save(translator);
        return new TranslatorResponse(translator.getId(), translator.getName(), translator.getEmail(), translator.getSourceLanguage(), translator.getTargetLanguage());
    }

    public TranslatorResponse update(UUID id, TranslatorRequest request) {
        Translator translator = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tradutor não encontrado."));

        if(Objects.equals(translator.getEmail(), request.email()) && translator.getId() != id)
            throw new IllegalArgumentException("O e-mail informado já está em uso por outro usuário. Informe outro e-mail.");

        translator.setName(request.name());
        translator.setEmail(request.email());
        translator.setSourceLanguage(request.sourceLanguage());
        translator.setTargetLanguage(request.targetLanguage());

        if (request.password() != null && !request.password().isBlank()) {
            String salt = SaltGenerator.saltGenerator();
            String hashedPassword = TranslatorPasswordEncoder.encode(request.password(), salt);
            translator.setPassword(hashedPassword);
            translator.setSalt(salt);
        }

        repository.save(translator);
        return new TranslatorResponse(translator.getId(), translator.getName(), translator.getEmail(), translator.getSourceLanguage(), translator.getTargetLanguage());
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Tradutor não encontrado.");
        }
        repository.deleteById(id);
    }

    public TranslatorResponse findById(UUID id) {
        Translator translator = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tradutor não encontrado."));
        return new TranslatorResponse(translator.getId(), translator.getName(), translator.getEmail(), translator.getSourceLanguage(), translator.getTargetLanguage());
    }

    public List<TranslatorResponse> findAll() {
        return repository.findAll().stream()
                .map(translator -> new TranslatorResponse(translator.getId(), translator.getName(), translator.getEmail(), translator.getSourceLanguage(), translator.getTargetLanguage()))
                .collect(Collectors.toList());
    }


}
