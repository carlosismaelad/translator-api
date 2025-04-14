package com.carlosdourado.translatorapi.application.services;

import com.carlosdourado.translatorapi.application.dtos.AuthResponse;
import com.carlosdourado.translatorapi.application.dtos.LoginRequest;
import com.carlosdourado.translatorapi.application.dtos.LoginResponse;
import com.carlosdourado.translatorapi.application.dtos.RegisterRequest;
import com.carlosdourado.translatorapi.domain.entities.Translator;
import com.carlosdourado.translatorapi.domain.repositories.TranslatorRepository;
import com.carlosdourado.translatorapi.infra.security.PasswordEncoder;
import com.carlosdourado.translatorapi.infra.security.SaltGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private TranslatorRepository repository;

    public AuthResponse register(RegisterRequest request){
        if(!request.password().equals(request.confirmPassword()))
            throw new IllegalArgumentException("Senha e confirmação não coincidem.");

        if (repository.findByEmail(request.email()).isPresent())
            throw new IllegalArgumentException("e-mail informado já está em uso.");

        String salt = SaltGenerator.saltGenerator();
        String hashedPassword = PasswordEncoder.encode(request.password(), salt);

        Translator translator = new Translator();
        translator.setName(request.name());
        translator.setEmail(request.email());
        translator.setSourceLanguage(request.sourceLanguage());
        translator.setTargetLanguage(request.targetLanguage());
        translator.setPassword(hashedPassword);
        translator.setSalt(salt);

        repository.save(translator);
        return  new AuthResponse("Usuário registrado com sucesso!", translator.getName(), translator.getEmail());
    }

    public LoginResponse login(LoginRequest request){
        Translator translator = repository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        String hashedInput = PasswordEncoder.encode(request.password(), translator.getSalt());

        if(!hashedInput.equals(translator.getPassword()))
            throw new IllegalArgumentException("Senha incorreta! Tente novamente.");

        return new LoginResponse("Login efetuado com sucesso!");
    }
}
