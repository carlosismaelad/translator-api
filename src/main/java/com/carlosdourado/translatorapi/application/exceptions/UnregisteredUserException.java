package com.carlosdourado.translatorapi.application.exceptions;

public class UnregisteredUserException extends RuntimeException{
    public UnregisteredUserException(String message){
        super(message);
    }
}
