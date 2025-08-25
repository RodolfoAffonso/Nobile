package com.rodolfoafonso.nobile.exception;

public class NotExistUserException extends RuntimeException {
    public NotExistUserException() {
        super("Usuario não Cadastrado.");
    }

    public NotExistUserException(String message) {
        super(message);
    }
}
