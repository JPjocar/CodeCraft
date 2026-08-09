package com.cosmos.CodeCraft.Exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super(String.format("El usuario '%s' ya esta registrado", username));
    }
}
