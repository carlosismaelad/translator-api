package com.carlosdourado.translatorapi.application.controllers;

import com.carlosdourado.translatorapi.application.dtos.registerDTOs.TranslatorRegisterResponse;
import com.carlosdourado.translatorapi.application.dtos.loginDTOs.LoginRequest;
import com.carlosdourado.translatorapi.application.dtos.loginDTOs.LoginResponse;
import com.carlosdourado.translatorapi.application.dtos.registerDTOs.TranslatorRegisterRequest;
import com.carlosdourado.translatorapi.application.services.register.TranslatorAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private TranslatorAuthService translatorAuthService;

    @PostMapping("/register")
    public TranslatorRegisterResponse register(@RequestBody TranslatorRegisterRequest request){
        return translatorAuthService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return translatorAuthService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String response = translatorAuthService.logout(token);
        return ResponseEntity.ok().body(response);
    }

}
