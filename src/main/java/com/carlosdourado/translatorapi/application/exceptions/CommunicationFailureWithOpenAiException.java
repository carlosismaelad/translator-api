package com.carlosdourado.translatorapi.application.exceptions;

public class CommunicationFailureWithOpenAiException extends RuntimeException{
    public CommunicationFailureWithOpenAiException(String message){
        super(message);
    }
}
