package com.etiya.etiyatelekom.common.exception.exceptions;

public class PasswordsDoNotMatchException extends RuntimeException {

    public PasswordsDoNotMatchException() {
        super("Passwords do not match");
    }
}
