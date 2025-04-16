package com.carlosdourado.translatorapi.application.exceptions;

public class TranslationProfileNotFoundException extends RuntimeException{
    public TranslationProfileNotFoundException(String message){
        super(message);
    }
}
