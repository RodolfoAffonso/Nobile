package com.rodolfoafonso.nobile.exception;

public class ExistingEmailException extends RuntimeException {

    public ExistingEmailException() {
        super("E-mail já está cadastrado.");
    }

    public ExistingEmailException(String message) {
        super(message);
    }
}
