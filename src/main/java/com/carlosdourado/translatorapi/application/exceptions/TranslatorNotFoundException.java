package com.carlosdourado.translatorapi.application.exceptions;

import java.util.UUID;

public class TranslatorNotFoundException extends RuntimeException{
    public TranslatorNotFoundException(UUID id){
        super("Traduto com id " + id + " não localizado.");
    }
}
