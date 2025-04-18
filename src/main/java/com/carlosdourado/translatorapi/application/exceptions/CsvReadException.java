package com.carlosdourado.translatorapi.application.exceptions;

public class CsvReadException extends RuntimeException{
    public CsvReadException(String message){
        super(message);
    }
}
