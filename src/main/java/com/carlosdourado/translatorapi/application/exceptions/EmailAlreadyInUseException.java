package com.carlosdourado.translatorapi.application.exceptions;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email){
        super("O email " + email + " já está em uso. Por favor, informe outro e-mail.");
    }
}
