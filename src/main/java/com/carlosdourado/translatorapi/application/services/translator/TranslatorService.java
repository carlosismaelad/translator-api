package com.carlosdourado.translatorapi.application.services.translator;

import com.carlosdourado.translatorapi.application.dtos.translatorDTOs.TranslatorRequest;
import com.carlosdourado.translatorapi.application.dtos.translatorDTOs.TranslatorResponse;
import com.carlosdourado.translatorapi.application.exceptions.EmailAlreadyInUseException;
import com.carlosdourado.translatorapi.application.exceptions.EmailBelongsToAnotherUserException;
import com.carlosdourado.translatorapi.application.exceptions.TranslatorNotFoundException;
import com.carlosdourado.translatorapi.domain.entities.Translator;
import com.carlosdourado.translatorapi.domain.repositories.TranslatorRepository;
import com.carlosdourado.translatorapi.infra.security.password.SaltGenerator;
import com.carlosdourado.translatorapi.infra.security.password.TranslatorPasswordEncoder;
import com.sun.jdi.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TranslatorService {
    @Autowired
    private TranslatorRepository repository;

    public TranslatorResponse create(TranslatorRequest request) {
        if (repository.findByEmail(request.email()).isPresent())
            throw new EmailAlreadyInUseException(request.email());

        String salt = SaltGenerator.saltGenerator();
        String hashedPassword = TranslatorPasswordEncoder.encode(request.password(), salt);

        Translator translator = new Translator();
        translator.setName(request.name());
        translator.setEmail(request.email());
        translator.setPassword(hashedPassword);
        translator.setSalt(salt);

        repository.save(translator);
        return new TranslatorResponse(translator.getId(), translator.getName(), translator.getEmail());
    }

    public TranslatorResponse update(UUID id, TranslatorRequest request) {
        Translator translator = repository.findById(id)
                .orElseThrow(() -> new TranslatorNotFoundException(id));

        var translatorExists = repository.findByEmail(request.email());
        if(translatorExists.isPresent() && !translatorExists.get().getId().equals(id))
            throw new EmailBelongsToAnotherUserException(request.email());

        translator.setName(request.name());
        translator.setEmail(request.email());

        if (request.password() != null && !request.password().isBlank()) {
            String salt = SaltGenerator.saltGenerator();
            String hashedPassword = TranslatorPasswordEncoder.encode(request.password(), salt);
            translator.setPassword(hashedPassword);
            translator.setSalt(salt);
        }

        repository.save(translator);
        return new TranslatorResponse(translator.getId(), translator.getName(), translator.getEmail());
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new TranslatorNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public TranslatorResponse findById(UUID id) {
        Translator translator = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tradutor não encontrado."));
        return new TranslatorResponse(translator.getId(), translator.getName(), translator.getEmail());
    }

    public List<TranslatorResponse> findAll() {
        return repository.findAll().stream()
                .map(translator -> new TranslatorResponse(translator.getId(), translator.getName(), translator.getEmail()))
                .collect(Collectors.toList());
    }


}
