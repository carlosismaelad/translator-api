package com.carlosdourado.translatorapi.application.exceptions;

public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(String message){
        super(message);
    }
}
