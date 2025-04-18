package com.carlosdourado.translatorapi.application.exceptions;

public class CredentialsNotFoundException extends RuntimeException {
    public CredentialsNotFoundException(String message){
        super(message);
    }
}
