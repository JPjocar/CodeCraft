package com.cosmos.CodeCraft.Exception;

/**
 * Se lanza cuando un nombre de archivo recibido del cliente intenta salir del
 * directorio de uploads (path traversal) o no es utilizable.
 */
public class InvalidFilenameException extends RuntimeException {

    public InvalidFilenameException(String message) {
        super(message);
    }
}
