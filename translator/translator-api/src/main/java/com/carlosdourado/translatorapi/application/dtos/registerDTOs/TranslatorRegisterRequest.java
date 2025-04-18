package com.carlosdourado.translatorapi.application.dtos.registerDTOs;

public record TranslatorRegisterRequest(String name, String email, String password, String confirmPassword) {
}
