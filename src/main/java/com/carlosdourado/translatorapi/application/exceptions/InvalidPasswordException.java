package com.carlosdourado.translatorapi.application.exceptions;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(){
        super("Senha ou e-mail incorreto!");
    }
}
