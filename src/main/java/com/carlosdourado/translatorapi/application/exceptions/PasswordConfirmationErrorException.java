package com.carlosdourado.translatorapi.application.exceptions;

public class PasswordConfirmationErrorException extends RuntimeException{
    public PasswordConfirmationErrorException(String message){
        super(message);
    }
}
