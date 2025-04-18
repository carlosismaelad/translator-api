package com.carlosdourado.translatorapi.application.services.register;

import com.carlosdourado.translatorapi.application.dtos.registerDTOs.TranslatorRegisterResponse;
import com.carlosdourado.translatorapi.application.dtos.loginDTOs.LoginRequest;
import com.carlosdourado.translatorapi.application.dtos.loginDTOs.LoginResponse;
import com.carlosdourado.translatorapi.application.dtos.registerDTOs.TranslatorRegisterRequest;
import com.carlosdourado.translatorapi.application.exceptions.*;
import com.carlosdourado.translatorapi.domain.entities.Translator;
import com.carlosdourado.translatorapi.domain.repositories.TranslatorRepository;
import com.carlosdourado.translatorapi.infra.security.authentication.JwtService;
import com.carlosdourado.translatorapi.infra.security.authentication.TokenBlackListInMemory;
import com.carlosdourado.translatorapi.infra.security.password.TranslatorPasswordEncoder;
import com.carlosdourado.translatorapi.infra.security.password.SaltGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranslatorAuthService {
    @Autowired
    private TranslatorRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenBlackListInMemory tokenBlackList;

    public TranslatorRegisterResponse register(TranslatorRegisterRequest request){
        if(!request.password().equals(request.confirmPassword()))
            throw new PasswordConfirmationErrorException("Senha e confirmação não coincidem.");

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
        return new TranslatorRegisterResponse("Novo tradutor registrado com sucesso!", translator.getName(), translator.getEmail());
    }

    public LoginResponse login(LoginRequest request){
        Translator translator = repository.findByEmail(request.email())
                .orElseThrow(() -> new UnregisteredUserException("Usuário não encontrado. Verifique se o e-mail está correto."));
        String hashedInput = TranslatorPasswordEncoder.encode(request.password(), translator.getSalt());

        if(!hashedInput.equals(translator.getPassword()))
            throw new InvalidPasswordException();

        String token = jwtService.generateToken(translator.getEmail());

        return new LoginResponse("Login efetuado com sucesso!", token);
    }

    public String logout(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("Erro ao realizar logout.");
        }

        tokenBlackList.add(token);
        return "Logout realizado com sucesso!";
    }
}
