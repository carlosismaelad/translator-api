package com.carlosdourado.translatorapi.application.exceptions;

public class BlankOrNullParametersException extends RuntimeException{
    public BlankOrNullParametersException(String message){
        super(message);
    }
}
