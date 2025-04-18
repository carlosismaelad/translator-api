package com.carlosdourado.translatorapi.application.exceptions;

public class EmailBelongsToAnotherUserException extends RuntimeException{
    public EmailBelongsToAnotherUserException(String email){
        super("O e-mail " + email + " já está em uso por outro usuário. Por favor informe outro e-mail.");
    }
}
